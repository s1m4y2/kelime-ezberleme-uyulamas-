// Generated by view binder compiler. Do not edit!
package com.example.uartek.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.uartek.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityResultBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button goToMenuButton;

  @NonNull
  public final TextView resultTextView;

  private ActivityResultBinding(@NonNull ConstraintLayout rootView, @NonNull Button goToMenuButton,
      @NonNull TextView resultTextView) {
    this.rootView = rootView;
    this.goToMenuButton = goToMenuButton;
    this.resultTextView = resultTextView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityResultBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityResultBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_result, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityResultBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.goToMenuButton;
      Button goToMenuButton = ViewBindings.findChildViewById(rootView, id);
      if (goToMenuButton == null) {
        break missingId;
      }

      id = R.id.resultTextView;
      TextView resultTextView = ViewBindings.findChildViewById(rootView, id);
      if (resultTextView == null) {
        break missingId;
      }

      return new ActivityResultBinding((ConstraintLayout) rootView, goToMenuButton, resultTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}