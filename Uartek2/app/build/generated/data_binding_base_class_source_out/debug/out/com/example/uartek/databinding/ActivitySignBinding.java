// Generated by view binder compiler. Do not edit!
package com.example.uartek.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.uartek.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySignBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button button9;

  @NonNull
  public final Button buttonSignUp;

  @NonNull
  public final EditText editTextConfirmPassword;

  @NonNull
  public final EditText editTextEmail;

  @NonNull
  public final EditText editTextPassword;

  private ActivitySignBinding(@NonNull ConstraintLayout rootView, @NonNull Button button9,
      @NonNull Button buttonSignUp, @NonNull EditText editTextConfirmPassword,
      @NonNull EditText editTextEmail, @NonNull EditText editTextPassword) {
    this.rootView = rootView;
    this.button9 = button9;
    this.buttonSignUp = buttonSignUp;
    this.editTextConfirmPassword = editTextConfirmPassword;
    this.editTextEmail = editTextEmail;
    this.editTextPassword = editTextPassword;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySignBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySignBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_sign, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySignBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.button9;
      Button button9 = ViewBindings.findChildViewById(rootView, id);
      if (button9 == null) {
        break missingId;
      }

      id = R.id.buttonSignUp;
      Button buttonSignUp = ViewBindings.findChildViewById(rootView, id);
      if (buttonSignUp == null) {
        break missingId;
      }

      id = R.id.editTextConfirmPassword;
      EditText editTextConfirmPassword = ViewBindings.findChildViewById(rootView, id);
      if (editTextConfirmPassword == null) {
        break missingId;
      }

      id = R.id.editTextEmail;
      EditText editTextEmail = ViewBindings.findChildViewById(rootView, id);
      if (editTextEmail == null) {
        break missingId;
      }

      id = R.id.editTextPassword;
      EditText editTextPassword = ViewBindings.findChildViewById(rootView, id);
      if (editTextPassword == null) {
        break missingId;
      }

      return new ActivitySignBinding((ConstraintLayout) rootView, button9, buttonSignUp,
          editTextConfirmPassword, editTextEmail, editTextPassword);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
