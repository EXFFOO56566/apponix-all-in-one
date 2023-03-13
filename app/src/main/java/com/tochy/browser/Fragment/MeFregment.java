package com.tochy.browser.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.tochy.browser.Activity.MusicActivity;
import com.tochy.browser.Activity.MyVideoActivity;
import com.tochy.browser.Activity.browser.BookamrksActivity;
import com.tochy.browser.Activity.browser.HistoryActivity;
import com.tochy.browser.Activity.browser.WebActivity;
import com.tochy.browser.R;
import com.tochy.browser.SessionManager;
import com.tochy.browser.ads.MyInterstitialAds;
import com.tochy.browser.databinding.FragmentMeFregmentBinding;
import com.tochy.browser.retrofit.Const;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class MeFregment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, MyInterstitialAds.InterAdClickListner {
    private static final String TAG = "googlerr";
    private static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    FragmentMeFregmentBinding binding;
    private FirebaseAuth mAuth;
    private SessionManager sessionManager;
    private MyInterstitialAds myInterstitialAds;
    private boolean isVideo = false;
    private boolean isHistory = false;
    private boolean isBookmarked = false;
    private boolean isExit = false;
    private boolean isMusic = false;

    public MeFregment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_me_fregment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = getActivity().getWindow().getDecorView().getSystemUiVisibility();
            flags |=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        sessionManager = new SessionManager(getActivity());
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        binding.btlogin.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);

        });
        myInterstitialAds = new MyInterstitialAds(getActivity(), this);
        initmain();


        if (sessionManager.getBooleanValue(Const.ISLOGIN)) {
            binding.btlogin.setVisibility(View.GONE);
            binding.tvemail.setText(sessionManager.getStringValue(Const.EMAIL));
            binding.tvusername.setText(sessionManager.getStringValue(Const.NAME));
            Glide.with(getActivity())
                    .load(sessionManager.getStringValue(Const.IMAGE))
                    .circleCrop()
                    .into(binding.ivuser);
        }
    }

    private void initmain() {
        binding.video.setOnClickListener(v -> {
            isVideo = true;
            myInterstitialAds.showAds();
            myInterstitialAds = new MyInterstitialAds(getActivity(), this);
        });

        binding.bookmarks.setOnClickListener(v -> {
            isBookmarked = true;
            myInterstitialAds.showAds();
            myInterstitialAds = new MyInterstitialAds(getActivity(), this);

        });


        binding.history.setOnClickListener(v -> {
            isHistory = true;
            myInterstitialAds.showAds();
            myInterstitialAds = new MyInterstitialAds(getActivity(), this);
        });


        binding.exit.setOnClickListener(v -> {
            isExit = true;
            myInterstitialAds.showAds();
            myInterstitialAds = new MyInterstitialAds(getActivity(), this);
        });

        binding.music.setOnClickListener(v -> {
            isMusic = true;
            myInterstitialAds.showAds();
            myInterstitialAds = new MyInterstitialAds(getActivity(), this);
        });

        binding.shereapp.setOnClickListener(v -> {
            final String appLink = "\nhttps://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
            Intent sendInt = new Intent(Intent.ACTION_SEND);
            sendInt.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            sendInt.putExtra(Intent.EXTRA_TEXT, "Humile - live video chat  Download Now  " + appLink);
            sendInt.setType("text/plain");
            startActivity(Intent.createChooser(sendInt, "Share"));
        });

        binding.privercy.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", Const.TERMS);
            startActivity(intent);
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                binding.tvemail.setText(account.getEmail());
                binding.tvusername.setText(account.getDisplayName());
                Glide.with(getActivity())
                        .load(account.getPhotoUrl())
                        .circleCrop()
                        .into(binding.ivuser);

                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        sessionManager.saveBooleanValue(Const.ISLOGIN, true);
                        binding.btlogin.setVisibility(View.GONE);
                        sessionManager.saveStringValue(Const.EMAIL, user.getEmail());
                        sessionManager.saveStringValue(Const.IMAGE, user.getPhotoUrl().toString());
                        sessionManager.saveStringValue(Const.NAME, user.getDisplayName());

                        Log.d(TAG, "onComplete: " + user.getEmail());
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Snackbar.make(binding.getRoot(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                    }

                    // ...
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult.toString());
    }

    @Override
    public void onAdClosed() {
        if (isVideo) {
            isVideo = false;
            startActivity(new Intent(getActivity(), MyVideoActivity.class));
        } else if (isHistory) {
            isHistory = false;
            sessionManager.saveBooleanValue(Const.isMe, true);
            Intent intent = new Intent(getActivity(), HistoryActivity.class);
            startActivity(intent);
        } else if (isBookmarked) {
            isBookmarked = false;
            sessionManager.saveBooleanValue(Const.isMe, true);
            Intent intent = new Intent(getActivity(), BookamrksActivity.class);
            startActivity(intent);
        } else if (isExit) {
            isExit = false;
            getActivity().finishAffinity();
        } else if (isMusic) {
            isMusic = false;
            startActivity(new Intent(getActivity(), MusicActivity.class));
        }


    }

    @Override
    public void onAdFail() {
        if (isVideo) {
            isVideo = false;
            startActivity(new Intent(getActivity(), MyVideoActivity.class));
        } else if (isHistory) {
            isHistory = false;
            sessionManager.saveBooleanValue(Const.isMe, true);
            Intent intent = new Intent(getActivity(), HistoryActivity.class);
            startActivity(intent);
        } else if (isBookmarked) {
            isBookmarked = false;
            sessionManager.saveBooleanValue(Const.isMe, true);
            Intent intent = new Intent(getActivity(), BookamrksActivity.class);
            startActivity(intent);
        } else if (isExit) {
            isExit = false;
            getActivity().finishAffinity();
        } else if (isMusic) {
            isMusic = false;
            startActivity(new Intent(getActivity(), MusicActivity.class));
        }
    }
}