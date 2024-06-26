// Generated by view binder compiler. Do not edit!
package com.example.uartek.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.uartek.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class RecyclerRowBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView recyclerviewRowEnglishText;

  @NonNull
  public final ImageView recyclerviewRowImageview;

  @NonNull
  public final TextView recyclerviewRowSentencesText;

  @NonNull
  public final TextView recyclerviewRowSubjectText;

  @NonNull
  public final TextView recyclerviewRowTurkishText;

  private RecyclerRowBinding(@NonNull LinearLayout rootView,
      @NonNull TextView recyclerviewRowEnglishText, @NonNull ImageView recyclerviewRowImageview,
      @NonNull TextView recyclerviewRowSentencesText, @NonNull TextView recyclerviewRowSubjectText,
      @NonNull TextView recyclerviewRowTurkishText) {
    this.rootView = rootView;
    this.recyclerviewRowEnglishText = recyclerviewRowEnglishText;
    this.recyclerviewRowImageview = recyclerviewRowImageview;
    this.recyclerviewRowSentencesText = recyclerviewRowSentencesText;
    this.recyclerviewRowSubjectText = recyclerviewRowSubjectText;
    this.recyclerviewRowTurkishText = recyclerviewRowTurkishText;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static RecyclerRowBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static RecyclerRowBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.recycler_row, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static RecyclerRowBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.recyclerview_row_english_text;
      TextView recyclerviewRowEnglishText = ViewBindings.findChildViewById(rootView, id);
      if (recyclerviewRowEnglishText == null) {
        break missingId;
      }

      id = R.id.recyclerview_row_imageview;
      ImageView recyclerviewRowImageview = ViewBindings.findChildViewById(rootView, id);
      if (recyclerviewRowImageview == null) {
        break missingId;
      }

      id = R.id.recyclerview_row_sentences_text;
      TextView recyclerviewRowSentencesText = ViewBindings.findChildViewById(rootView, id);
      if (recyclerviewRowSentencesText == null) {
        break missingId;
      }

      id = R.id.recyclerview_row_subject_text;
      TextView recyclerviewRowSubjectText = ViewBindings.findChildViewById(rootView, id);
      if (recyclerviewRowSubjectText == null) {
        break missingId;
      }

      id = R.id.recyclerview_row_turkish_text;
      TextView recyclerviewRowTurkishText = ViewBindings.findChildViewById(rootView, id);
      if (recyclerviewRowTurkishText == null) {
        break missingId;
      }

      return new RecyclerRowBinding((LinearLayout) rootView, recyclerviewRowEnglishText,
          recyclerviewRowImageview, recyclerviewRowSentencesText, recyclerviewRowSubjectText,
          recyclerviewRowTurkishText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
