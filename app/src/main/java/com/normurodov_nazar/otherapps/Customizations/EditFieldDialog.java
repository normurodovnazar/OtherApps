package com.normurodov_nazar.otherapps.Customizations;

import static com.normurodov_nazar.otherapps.Sources.PublicData.currentMusic;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.normurodov_nazar.otherapps.Listeners.SuccessListener;
import com.normurodov_nazar.otherapps.R;

public class EditFieldDialog extends Dialog {
    final SuccessListener successListener;
    EditText title,artist,album;
    Button button;
    String titleText,artistText,albumText;

    public EditFieldDialog(@NonNull Context context, SuccessListener successListener) {
        super(context);
        this.successListener = successListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout_dialog);
        button = findViewById(R.id.edit);
        title = findViewById(R.id.title_edit);
        artist = findViewById(R.id.artist_edit);
        album = findViewById(R.id.album_edit);

        title.setText(currentMusic.getTitle());
        artist.setText(currentMusic.getArtist());
        album.setText(currentMusic.getAlbum());

        button.setOnClickListener(c -> {
            titleText = title.getText().toString();
            artistText = artist.getText().toString();
            albumText = album.getText().toString();
            titleText = formattedText(titleText);
            artistText = formattedText(artistText);
            albumText = formattedText(albumText);
            successListener.onSuccess(new String[]{titleText,artistText,albumText});
            dismiss();
        });
    }

    private String formattedText(String text){
        String a = text;
        if (!a.isEmpty()) {
            while (a.charAt(a.length()-1)==' ') a = removeLast(a);
            while (a.charAt(0)==' ') a = removeFirst(a);
        } else Toast.makeText(getContext(), R.string.typeSomething, Toast.LENGTH_SHORT).show();
        return a;
    }

    private String removeFirst(String text) {
        return text.replace(" ","");
    }

    private String removeLast(String text) {
        StringBuilder temp = new StringBuilder();
        String[] strings = text.split("");
        for (int i=0;i<strings.length-1;i++) temp.append(strings[i]);
        return temp.toString();
    }
}
