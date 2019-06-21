package com.example.cliqv1;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

//import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
//import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
//import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class EmojiconLibrary extends AppCompatActivity {

    View rootView;
    ImageButton emojiButton;
    ImageButton submit_button;
    CheckBox checkBoxDefault;
  //  EmojiconEditText emojiconEdit;
  //  EmojiconTextView textContent;
  //  EmojIconActions emojIconActions;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_emojicon);

        rootView = (RelativeLayout) findViewById(R.id.library_emojicon);
        emojiButton = (ImageButton) findViewById(R.id.emojiButton);
        submit_button = (ImageButton) findViewById(R.id.submit_button);
        checkBoxDefault = (CheckBox) findViewById(R.id.checkBoxDefault);
     //   emojiconEdit = (EmojiconEditText) findViewById(R.id.emojiconEdit);
     //   textContent = (EmojiconTextView) findViewById(R.id.textContent);
     //   emojIconActions = new EmojIconActions(getApplicationContext(), rootView, emojiButton, emojiconEdit);

     //   emojIconActions.ShowEmojicon();
        checkBoxDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    //            emojIconActions.setUseSystemEmoji(b);
    //            textContent.setUseSystemDefault(b);

            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
     //           String message = emojiconEdit.getText().toString();
     //           textContent.setText(message);
            }
        });
    }
}
// -