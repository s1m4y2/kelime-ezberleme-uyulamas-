// Generated by view binder compiler. Do not edit!
package com.example.uartek.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.uartek.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAddWordBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextInputEditText EnglishText;

  @NonNull
  public final TextInputLayout EnglishTextHint;

  @NonNull
  public final TextInputEditText TurkishText;

  @NonNull
  public final TextInputLayout TurkishTextHint;

  @NonNull
  public final Button button9;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final Spinner mainActivitySpinnerGender;

  @NonNull
  public final Button saveWordButton;

  @NonNull
  public final TextInputEditText sentencesText;

  @NonNull
  public final TextInputLayout sentencesTextHint;

  private ActivityAddWordBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextInputEditText EnglishText, @NonNull TextInputLayout EnglishTextHint,
      @NonNull TextInputEditText TurkishText, @NonNull TextInputLayout TurkishTextHint,
      @NonNull Button button9, @NonNull ImageView imageView,
      @NonNull Spinner mainActivitySpinnerGender, @NonNull Button saveWordButton,
      @NonNull TextInputEditText sentencesText, @NonNull TextInputLayout sentencesTextHint) {
    this.rootView = rootView;
    this.EnglishText = EnglishText;
    this.EnglishTextHint = EnglishTextHint;
    this.TurkishText = TurkishText;
    this.TurkishTextHint = TurkishTextHint;
    this.button9 = button9;
    this.imageView = imageView;
    this.mainActivitySpinnerGender = mainActivitySpinnerGender;
    this.saveWordButton = saveWordButton;
    this.sentencesText = sentencesText;
    this.sentencesTextHint = sentencesTextHint;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAddWordBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAddWordBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_add_word, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAddWordBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.English_text;
      TextInputEditText EnglishText = ViewBindings.findChildViewById(rootView, id);
      if (EnglishText == null) {
        break missingId;
      }

      id = R.id.English_text_hint;
      TextInputLayout EnglishTextHint = ViewBindings.findChildViewById(rootView, id);
      if (EnglishTextHint == null) {
        break missingId;
      }

      id = R.id.Turkish_text;
      TextInputEditText TurkishText = ViewBindings.findChildViewById(rootView, id);
      if (TurkishText == null) {
        break missingId;
      }

      id = R.id.Turkish_text_hint;
      TextInputLayout TurkishTextHint = ViewBindings.findChildViewById(rootView, id);
      if (TurkishTextHint == null) {
        break missingId;
      }

      id = R.id.button9;
      Button button9 = ViewBindings.findChildViewById(rootView, id);
      if (button9 == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.main_activity_spinnerGender;
      Spinner mainActivitySpinnerGender = ViewBindings.findChildViewById(rootView, id);
      if (mainActivitySpinnerGender == null) {
        break missingId;
      }

      id = R.id.save_word_button;
      Button saveWordButton = ViewBindings.findChildViewById(rootView, id);
      if (saveWordButton == null) {
        break missingId;
      }

      id = R.id.sentences_text;
      TextInputEditText sentencesText = ViewBindings.findChildViewById(rootView, id);
      if (sentencesText == null) {
        break missingId;
      }

      id = R.id.sentences_text_hint;
      TextInputLayout sentencesTextHint = ViewBindings.findChildViewById(rootView, id);
      if (sentencesTextHint == null) {
        break missingId;
      }

      return new ActivityAddWordBinding((ConstraintLayout) rootView, EnglishText, EnglishTextHint,
          TurkishText, TurkishTextHint, button9, imageView, mainActivitySpinnerGender,
          saveWordButton, sentencesText, sentencesTextHint);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}