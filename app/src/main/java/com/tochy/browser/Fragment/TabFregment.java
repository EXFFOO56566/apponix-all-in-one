package com.tochy.browser.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.tochy.browser.Activity.browser.BookamrksActivity;
import com.tochy.browser.Activity.browser.HistoryActivity;
import com.tochy.browser.MainActivity;
import com.tochy.browser.R;
import com.tochy.browser.SessionManager;
import com.tochy.browser.databinding.FragmentTabFregmentBinding;
import com.tochy.browser.databinding.TabEditTextBinding;
import com.tochy.browser.retrofit.Const;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Locale;

import de.mrapp.android.tabswitcher.AbstractState;
import de.mrapp.android.tabswitcher.AddTabButtonListener;
import de.mrapp.android.tabswitcher.Animation;
import de.mrapp.android.tabswitcher.Layout;
import de.mrapp.android.tabswitcher.PeekAnimation;
import de.mrapp.android.tabswitcher.PullDownGesture;
import de.mrapp.android.tabswitcher.RevealAnimation;
import de.mrapp.android.tabswitcher.StatefulTabSwitcherDecorator;
import de.mrapp.android.tabswitcher.SwipeGesture;
import de.mrapp.android.tabswitcher.Tab;
import de.mrapp.android.tabswitcher.TabPreviewListener;
import de.mrapp.android.tabswitcher.TabSwitcher;
import de.mrapp.android.tabswitcher.TabSwitcherListener;
import de.mrapp.android.util.ThemeUtil;
import de.mrapp.android.util.multithreading.AbstractDataBinder;
import de.mrapp.util.Condition;

import static android.app.Activity.RESULT_OK;
import static androidx.databinding.DataBindingUtil.inflate;
import static com.tochy.browser.Activity.browser.BookamrksActivity.BOOKMAR_ACTIVITY_REQUEST_CODE;
import static com.tochy.browser.Activity.browser.HistoryActivity.HISTORY_ACTIVITY_CODE;
import static de.mrapp.android.util.DisplayUtil.getDisplayWidth;


public class TabFregment extends Fragment implements TabSwitcherListener, MainActivity.ClickInterface {

    private static final String GOOGLE_URL = "https://www.google.com";
    private static final String TAG = "tab";
    //
    /**
     * The name of the extra, which is used to store the view type of a tab within a bundle.
     */
    private static final String VIEW_TYPE_EXTRA = TabFregment.class.getName() + "::ViewType";
    /**
     * The name of the extra, which is used to store the state of a list adapter within a bundle.
     */
    private static final String ADAPTER_STATE_EXTRA = State.class.getName() + "::%s::AdapterState";
    /**
     * The number of tabs, which are contained by the example app's tab switcher.
     */
    private static final int TAB_COUNT = 1;
    private static final int SPEECH_REQUEST_CODE = 2;
    String baseGoogleSearch = "https://www.google.com/search?q=";
    FragmentTabFregmentBinding binding;
    /**
     * The activity's tab switcher.
     */
    private TabSwitcher tabSwitcher;
    /**
     * The decorator of the activity's tab switcher.
     */
    private Decorator decorator;
    MainActivity mainActivity;
    /**
     * The data binder, which is used to load the list items of tabs.
     */
    private DataBinder dataBinder;
    private SessionManager sessonManager;

    private TabEditTextBinding tabEditTextBinding;
    /**
     * The activity's snackbar.
     */


    private Snackbar snackbar;

    public TabFregment() {
        // Required empty public constructor
    }

    /**
     * Creates a listener, which allows to apply the window insets to the tab switcher's padding.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnApplyWindowInsetsListener}. The listener may not be nullFG
     */
    @NonNull
    private OnApplyWindowInsetsListener createWindowInsetsListener() {
        return (v, insets) -> {
            int left = insets.getSystemWindowInsetLeft();
            int top = insets.getSystemWindowInsetTop();
            int right = insets.getSystemWindowInsetRight();
            int bottom = insets.getSystemWindowInsetBottom();
            tabSwitcher.setPadding(left, top, right, bottom);
            float touchableAreaTop = top;

            if (tabSwitcher.getLayout() == Layout.TABLET) {
                touchableAreaTop += getResources()
                        .getDimensionPixelSize(R.dimen.tablet_tab_container_height);
            }

            RectF touchableArea = new RectF(left, touchableAreaTop,
                    getDisplayWidth(getActivity()) - right, touchableAreaTop +
                    ThemeUtil.getDimensionPixelSize(getActivity(), R.attr.actionBarSize));
            tabSwitcher.addDragGesture(
                    new SwipeGesture.Builder().setTouchableArea(touchableArea).create());
            tabSwitcher.addDragGesture(
                    new PullDownGesture.Builder().setTouchableArea(touchableArea).create());
            return insets;
        };
    }

    /**
     * Creates and returns a listener, which allows to add a tab to the activity's tab switcher,
     * when a button is clicked.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * View.OnClickListener}. The listener may not be null
     */
    @NonNull
    private View.OnClickListener createAddTabListener() {
        return view -> {
            int index = tabSwitcher.getCount();
            Animation animation = createRevealAnimation();
            tabSwitcher.addTab(createTab(index), 0, animation);
        };
    }

    /**
     * Creates and returns a listener, which allows to observe, when an item of the tab switcher's
     * toolbar has been clicked.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * Toolbar.OnMenuItemClickListener}. The listener may not be null
     */
    @NonNull
    private Toolbar.OnMenuItemClickListener createToolbarMenuListener() {
        return item -> {
            switch (item.getItemId()) {
                case R.id.remove_tab_menu_item:
                    Tab selectedTab = tabSwitcher.getSelectedTab();

                    if (selectedTab != null) {
                        tabSwitcher.removeTab(selectedTab);
                    }

                    return true;
                case R.id.add_tab_menu_item:
                    int index = tabSwitcher.getCount();
                    Tab tab = createTab(index);

                    if (tabSwitcher.isSwitcherShown()) {
                        tabSwitcher.addTab(tab, 0, createRevealAnimation());
                    } else {
                        tabSwitcher.addTab(tab, 0, createPeekAnimation());
                    }
                    return true;
                case R.id.clear_tabs_menu_item:
                    tabSwitcher.clear();
                    return true;
                case R.id.add_to_bookmarks:
                    sessonManager.saveBooleanValue(Const.isTab, true);
                    Intent intent = new Intent(getActivity(), BookamrksActivity.class);
                    startActivityForResult(intent, BOOKMAR_ACTIVITY_REQUEST_CODE);
                    return true;
                case R.id.history:
                    sessonManager.saveBooleanValue(Const.isTab, true);
                    Intent intent2 = new Intent(getActivity(), HistoryActivity.class);
                    startActivityForResult(intent2, HISTORY_ACTIVITY_CODE);
                    return true;
                default:
                    return false;
            }
        };
    }

    /**
     * Inflates the tab switcher's menu, depending on whether it is empty, or not.
     */
    private void inflateMenu() {
        tabSwitcher.inflateToolbarMenu(tabSwitcher.getCount() > 0 ? R.menu.tab_switcher : R.menu.tab,
                createToolbarMenuListener());
    }

    /**
     * Creates and returns a listener, which allows to toggle the visibility of the tab switcher,
     * when a button is clicked.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * View.OnClickListener}. The listener may not be null
     */
    @NonNull
    private View.OnClickListener createTabSwitcherButtonListener() {
        return view -> tabSwitcher.toggleSwitcherVisibility();
    }

    /**
     * Creates and returns a listener, which allows to add a new tab to the tab switcher, when the
     * corresponding button is clicked.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * AddTabButtonListener}. The listener may not be null
     */
    @NonNull
    private AddTabButtonListener createAddTabButtonListener() {
        return tabSwitcher -> {
            int index = tabSwitcher.getCount();
            Tab tab = createTab(index);
            tabSwitcher.addTab(tab, 0);
        };
    }

    /**
     * Creates and returns a listener, which allows to undo the removal of tabs from the tab
     * switcher, when the button of the activity's snackbar is clicked.
     *
     * @param snackbar The activity's snackbar as an instance of the class {@link Snackbar}. The snackbar
     *                 may not be null
     * @param index    The index of the first tab, which has been removed, as an {@link Integer} value
     * @param tabs     An array, which contains the tabs, which have been removed, as an array of the type
     *                 {@link Tab}. The array may not be null
     * @return The listener, which has been created, as an instance of the type {@link
     * View.OnClickListener}. The listener may not be null
     */
    @NonNull
    private View.OnClickListener createUndoSnackbarListener(@NonNull final Snackbar snackbar,
                                                            final int index,
                                                            @NonNull final Tab... tabs) {
        return view -> {
            snackbar.setAction(null, null);

            if (tabSwitcher.isSwitcherShown()) {
                tabSwitcher.addAllTabs(tabs, index);
            } else if (tabs.length == 1) {
                tabSwitcher.addTab(tabs[0], 0, createPeekAnimation());
            }

        };
    }

    /**
     * Creates and returns a callback, which allows to observe, when a snackbar, which allows to
     * undo the removal of tabs, has been dismissed.
     *
     * @param tabs An array, which contains the tabs, which have been removed, as an array of the type
     *             {@link Tab}. The tab may not be null
     * @return The callback, which has been created, as an instance of the type class {@link
     * BaseTransientBottomBar.BaseCallback}. The callback may not be null
     */
    @NonNull
    private BaseTransientBottomBar.BaseCallback<Snackbar> createUndoSnackbarCallback(
            final Tab... tabs) {
        return new BaseTransientBottomBar.BaseCallback<Snackbar>() {

            @Override
            public void onDismissed(final Snackbar snackbar, final int event) {
                if (event != DISMISS_EVENT_ACTION) {
                    for (Tab tab : tabs) {
                        tabSwitcher.clearSavedState(tab);
                        decorator.clearState(tab);
                    }
                }
            }
        };
    }

    /**
     * Shows a snackbar, which allows to undo the removal of tabs from the activity's tab switcher.
     *
     * @param text  The text of the snackbar as an instance of the type {@link CharSequence}. The text
     *              may not be null
     * @param index The index of the first tab, which has been removed, as an {@link Integer} value
     * @param tabs  An array, which contains the tabs, which have been removed, as an array of the type
     *              {@link Tab}. The array may not be null
     */
    private void showUndoSnackbar(@NonNull final CharSequence text, final int index,
                                  @NonNull final Tab... tabs) {
        snackbar = Snackbar.make(tabSwitcher, text, Snackbar.LENGTH_LONG).setActionTextColor(
                ContextCompat.getColor(getActivity(), R.color.snackbar_action_text_color));
        snackbar.setAction(R.string.undo, createUndoSnackbarListener(snackbar, index, tabs));
        snackbar.addCallback(createUndoSnackbarCallback(tabs));
        snackbar.show();
    }

    /**
     * Creates a reveal animation, which can be used to add a tab to the activity's tab switcher.
     *
     * @return The reveal animation, which has been created, as an instance of the class {@link
     * Animation}. The animation may not be null
     */
    @NonNull
    private Animation createRevealAnimation() {
        float x = 0;
        float y = 0;
        View view = getNavigationMenuItem();

        if (view != null) {
            int[] location = new int[2];
            view.getLocationInWindow(location);
            x = location[0] + (view.getWidth() / 2f);
            y = location[1] + (view.getHeight() / 2f);
        }

        return new RevealAnimation.Builder().setX(x).setY(y).create();
    }

    /**
     * Creates a peek animation, which can be used to add a tab to the activity's tab switcher.
     *
     * @return The peek animation, which has been created, as an instance of the class {@link
     * Animation}. The animation may not be null
     */
    @NonNull
    private Animation createPeekAnimation() {
        return new PeekAnimation.Builder().setX(tabSwitcher.getWidth() / 2f).create();
    }

    /**
     * Returns the menu item, which shows the navigation icon of the tab switcher's toolbar.
     *
     * @return The menu item, which shows the navigation icon of the tab switcher's toolbar, as an
     * instance of the class {@link View} or null, if no navigation icon is shown
     */
    @Nullable
    private View getNavigationMenuItem() {
        Toolbar[] toolbars = tabSwitcher.getToolbars();

        if (toolbars != null) {
            Toolbar toolbar = toolbars.length > 1 ? toolbars[1] : toolbars[0];
            int size = toolbar.getChildCount();

            for (int i = 0; i < size; i++) {
                View child = toolbar.getChildAt(i);

                if (child instanceof ImageButton) {
                    return child;
                }
            }
        }

        return null;
    }

    /**
     * Creates and returns a tab.
     *
     * @param index The index, the tab should be added at, as an {@link Integer} value
     * @return The tab, which has been created, as an instance of the class {@link Tab}. The tab may
     * not be null
     */
    @NonNull
    private Tab createTab(final int index) {
        CharSequence title = getString(R.string.tab_title, index + 1);
        Tab tab = new Tab(title);
        Bundle parameters = new Bundle();
        parameters.putInt(VIEW_TYPE_EXTRA, index % 3);
        tab.setParameters(parameters);
        return tab;
    }

    @Override
    public final void onSwitcherShown(@NonNull final TabSwitcher tabSwitcher) {
//
    }

    @Override
    public final void onSwitcherHidden(@NonNull final TabSwitcher tabSwitcher) {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override
    public final void onSelectionChanged(@NonNull final TabSwitcher tabSwitcher,
                                         final int selectedTabIndex,
                                         @Nullable final Tab selectedTab) {
//
    }

    @Override
    public final void onTabAdded(@NonNull final TabSwitcher tabSwitcher, final int index,
                                 @NonNull final Tab tab, @NonNull final Animation animation) {
        inflateMenu();
        TabSwitcher.setupWithMenu(tabSwitcher, createTabSwitcherButtonListener());
    }

    @Override
    public final void onTabRemoved(@NonNull final TabSwitcher tabSwitcher, final int index,
                                   @NonNull final Tab tab, @NonNull final Animation animation) {
        CharSequence text = getString(R.string.removed_tab_snackbar, tab.getTitle());
        showUndoSnackbar(text, index, tab);
        inflateMenu();
        TabSwitcher.setupWithMenu(tabSwitcher, createTabSwitcherButtonListener());
    }

    @Override
    public final void onAllTabsRemoved(@NonNull final TabSwitcher tabSwitcher,
                                       @NonNull final Tab[] tabs,
                                       @NonNull final Animation animation) {
        CharSequence text = getString(R.string.cleared_tabs_snackbar);
        showUndoSnackbar(text, 0, tabs);
        inflateMenu();
        TabSwitcher.setupWithMenu(tabSwitcher, createTabSwitcherButtonListener());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, R.layout.fragment_tab_fregment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = getActivity().getWindow().getDecorView().getSystemUiVisibility();
            flags |=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        return binding.getRoot();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataBinder = new DataBinder(getActivity());
        decorator = new Decorator();
        tabSwitcher = getActivity().findViewById(R.id.tab_switcher);
        tabSwitcher.clearSavedStatesWhenRemovingTabs(false);
        ViewCompat.setOnApplyWindowInsetsListener(tabSwitcher, createWindowInsetsListener());
        tabSwitcher.setDecorator(decorator);
        tabSwitcher.addListener(this);
        tabSwitcher.showToolbars(true);

        for (int i = 0; i < TAB_COUNT; i++) {
            tabSwitcher.addTab(createTab(i));
        }

        tabSwitcher.showAddTabButton(createAddTabButtonListener());
        tabSwitcher.setToolbarNavigationIcon(R.drawable.ic_plus_24dp, createAddTabListener());
        TabSwitcher.setupWithMenu(tabSwitcher, createTabSwitcherButtonListener());
        inflateMenu();


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void handleTabView(TabEditTextBinding binding) {


        binding.progressbar.setIndeterminate(true);
        binding.progressbar.setVisibility(View.GONE);
        binding.progressbar.setIndeterminateTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.blue)));


        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(binding.etSearch.getWindowToken(), 0);

        sessonManager = new SessionManager(getActivity());

        binding.googleMic.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        });


        loadUrl(GOOGLE_URL, binding);


        binding.refres.setOnClickListener(v -> {
            binding.progressbar.setVisibility(View.VISIBLE);
            loadUrl(binding.etSearch.getText().toString(), binding);
        });


        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String text = binding.etSearch.getText().toString();
                if (text.contains("https://") || text.contains("http://") || text.contains(".com")) {
                    loadUrl(binding.etSearch.getText().toString(), binding);
                } else {
                    loadUrl(baseGoogleSearch + text, binding);

                }

                in.hideSoftInputFromWindow(binding.etSearch.getWindowToken(), 0);
                binding.etSearch.clearFocus();
                return true;
            }
            return false;
        });


    }

    private void loadUrl(String url, TabEditTextBinding binding) {
        if (url != null) {
            binding.webView.loadUrl(url);
            binding.webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                    view.loadUrl(urlNewString);
                    binding.etSearch.setText(urlNewString);
                    binding.progressbar.setVisibility(View.VISIBLE);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap facIcon) {

                    //SHOW LOADING IF IT ISNT ALREADY VISIBLE

                    addToHistry(url);

                    binding.etSearch.setText(url);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    binding.etSearch.setText(url);

                    binding.progressbar.setVisibility(View.GONE);

                }
            });

        }
    }

    private void addToHistry(String url) {
        sessonManager.addToHistory(url);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = null;
            if (data != null) {
                results = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
            }
            String spokenText = null;
            if (results != null) {
                spokenText = results.get(0);
            }
            // Do something with spokenText
            tabEditTextBinding.etSearch.setText(baseGoogleSearch + spokenText);
            loadUrl(baseGoogleSearch + spokenText, tabEditTextBinding);

        }

        if (requestCode == BOOKMAR_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: requestcode " + requestCode);
                Log.d(TAG, "onActivityResult: result  " + resultCode);
                if (data != null) {
                    tabEditTextBinding.progressbar.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onActivityResult: datawork " + data.getStringExtra("url"));
                    tabEditTextBinding.etSearch.setText(data.getStringExtra("url"));
                    loadUrl(data.getStringExtra("url"), tabEditTextBinding);
                }

            }


        }

        if (requestCode == HISTORY_ACTIVITY_CODE && resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: requestcode " + requestCode);
                Log.d(TAG, "onActivityResult: result  " + resultCode);
                if (data != null) {
                    tabEditTextBinding.progressbar.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onActivityResult: datawork " + data.getStringExtra("url"));
                    tabEditTextBinding.etSearch.setText(data.getStringExtra("url"));
                    loadUrl(data.getStringExtra("url"), tabEditTextBinding);
                }

            }


        }
    }

    @Override
    public void buttonClicked() {

        tabSwitcher.toggleSwitcherVisibility();
    }

    /**
     * A data binder, which is used to asynchronously load the list items, which are displayed by a
     * tab.
     */
    private static class DataBinder
            extends AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> {

        /**
         * Creates a new data binder, which is used to asynchronously load the list items, which are
         * displayed by a tab.
         *
         * @param context The context, which should be used by the data binder, as an instance of the class
         *                {@link Context}. The context may not be null
         */
        public DataBinder(@NonNull final Context context) {
            super(context.getApplicationContext());
        }

        @Nullable
        @Override
        protected ArrayAdapter<String> doInBackground(@NonNull final Tab key,
                                                      @NonNull final Void... params) {
            String[] array = new String[10];

            for (int i = 0; i < array.length; i++) {
                array[i] = String.format(Locale.getDefault(), "%s, item %d", key.getTitle(), i + 1);
            }

            try {
                //
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //
            }

            return new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, array);
        }

        @Override
        protected void onPostExecute(@NonNull final ListView view,
                                     @Nullable final ArrayAdapter<String> data, final long duration,
                                     @NonNull final Void... params) {
            if (data != null) {
                view.setAdapter(data);
            }
        }

    }

    private class State extends AbstractState
            implements AbstractDataBinder.Listener<ArrayAdapter<String>, Tab, ListView, Void>,
            TabPreviewListener {
        private ArrayAdapter<String> adapter;

        State(@NonNull final Tab tab) {
            super(tab);
        }


        public void loadItems(@NonNull final ListView listView) {
            Condition.INSTANCE.ensureNotNull(listView, "The list view may not be null");

            if (adapter == null) {
                dataBinder.addListener(this);
                dataBinder.load(getTab(), listView);
            } else if (listView.getAdapter() != adapter) {
                listView.setAdapter(adapter);
            }
        }

        @Override
        public boolean onLoadData(
                @NonNull final AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> dataBinder,
                @NonNull final Tab key, @NonNull final Void... params) {
            return true;
        }

        @Override
        public void onCanceled(
                @NonNull final AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> dataBinder) {
//
        }

        @Override
        public void onFinished(
                @NonNull final AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> dataBinder,
                @NonNull final Tab key, @Nullable final ArrayAdapter<String> data,
                @NonNull final ListView view, @NonNull final Void... params) {
            if (getTab().equals(key)) {
                view.setAdapter(data);
                adapter = data;
                dataBinder.removeListener(this);
            }
        }

        @Override
        public final void saveInstanceState(@NonNull final Bundle outState) {
            if (adapter != null && !adapter.isEmpty()) {
                String[] array = new String[adapter.getCount()];

                for (int i = 0; i < array.length; i++) {
                    array[i] = adapter.getItem(i);
                }

                outState.putStringArray(String.format(ADAPTER_STATE_EXTRA, getTab().getTitle()),
                        array);
            }
        }

        @Override
        public void restoreInstanceState(@Nullable final Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                String key = String.format(ADAPTER_STATE_EXTRA, getTab().getTitle());
                String[] items = savedInstanceState.getStringArray(key);

                if (items != null && items.length > 0) {
                    adapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_list_item_1, items);
                }
            }
        }

        @Override
        public boolean onLoadTabPreview(@NonNull final TabSwitcher tabSwitcher,
                                        @NonNull final Tab tab) {
            return !getTab().equals(tab) || adapter != null;
        }

    }

    /**
     * The decorator, which is used to inflate and visualize the tabs of the activity's tab
     * switcher.
     */
    private class Decorator extends StatefulTabSwitcherDecorator<State> {

        @Nullable
        @Override
        protected State onCreateState(@NonNull final Context context,
                                      @NonNull final TabSwitcher tabSwitcher,
                                      @NonNull final View view, @NonNull final Tab tab,
                                      final int index, final int viewType,
                                      @Nullable final Bundle savedInstanceState) {
            if (viewType == 2) {
                State state = new State(tab);
                tabSwitcher.addTabPreviewListener(state);

                if (savedInstanceState != null) {
                    state.restoreInstanceState(savedInstanceState);
                }

                return state;
            }

            return null;
        }

        @Override
        protected void onClearState(@NonNull final State state) {
            tabSwitcher.removeTabPreviewListener(state);
        }

        @Override
        protected void onSaveInstanceState(@NonNull final View view, @NonNull final Tab tab,
                                           final int index, final int viewType,
                                           @Nullable final State state,
                                           @NonNull final Bundle outState) {
            if (state != null) {
                state.saveInstanceState(outState);
            }
        }

        @NonNull
        @Override
        public View onInflateView(@NonNull final LayoutInflater inflater,
                                  @Nullable final ViewGroup parent, final int viewType) {
            View view;

            if (viewType == 0) {
                view = inflater.inflate(R.layout.tab_edit_text, parent, false);
                tabEditTextBinding = TabEditTextBinding.bind(view);
                handleTabView(tabEditTextBinding);
            } else if (viewType == 1) {
                view = inflater.inflate(R.layout.tab_text_view, parent, false);
            } else {
                view = inflater.inflate(R.layout.tab_list_view, parent, false);
            }


            Toolbar toolbar = view.findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.tab);
            toolbar.setOnMenuItemClickListener(createToolbarMenuListener());
            Menu menu = toolbar.getMenu();
            TabSwitcher.setupWithMenu(tabSwitcher, menu, createTabSwitcherButtonListener());
            return view;
        }

        @Override
        public void onShowTab(@NonNull final Context context,
                              @NonNull final TabSwitcher tabSwitcher, @NonNull final View view,
                              @NonNull final Tab tab, final int index, final int viewType,
                              @Nullable final State state,
                              @Nullable final Bundle savedInstanceState) {
            TextView textView = findViewById(android.R.id.title);
            textView.setText(tab.getTitle());
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setVisibility(tabSwitcher.isSwitcherShown() ? View.GONE : View.VISIBLE);

            if (viewType == 0) {

                EditText editText = findViewById(android.R.id.edit);

                if (savedInstanceState == null) {
                    editText.setText(null);
                }

                editText.requestFocus();


            } else if (viewType == 1 && state != null) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contener, new HomeFregment()).commit();
            } else if (viewType == 2 && state != null) {
                ListView listView = findViewById(android.R.id.list);
                state.loadItems(listView);
            }
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getViewType(@NonNull final Tab tab, final int index) {
            return 0;
        }
    }

}