package net.mssc.redessocialesandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ShareButton compartirFoto, compartirLink, compartirPublicacion;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* OBTENER LA CLAVE
        try {
            PackageInfo info = getPackageManager().getPackageInfo("net.mssc.redessocialesandroid", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("FIRMA", sign);
                Toast.makeText(getApplicationContext(), sign, Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        } */

        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        compartirLink = findViewById(R.id.shareLink);
        compartirFoto = findViewById(R.id.sharePhoto);
        compartirPublicacion = findViewById(R.id.sharePost);
        imageView = findViewById(R.id.imgCompartir);
        imageView.setImageResource(R.drawable.foto);

        //IDENTIFICAR SI HAY SESION INICIADA
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        //SI HAY SESIÓN INICIADA, SE INICIALIZAN LOS SHARE BUTTON
        if (isLoggedIn) {
            Log.d("AGUS", "Usuario ya con sesion");

            //COMPARTIR PUBLICACION
            //El link está vacio para que abra una publicación vacia
            ShareLinkContent post = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(""))
                    .build();
            compartirPublicacion.setShareContent(post);

            //COMPARTIR LINK
            //Esta publicacion comparte un video de youtube con una cita y un hastag
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://youtu.be/OsfAnsMY21M"))
                    .setQuote("I see us written in the stars") //cita
                    .setShareHashtag(new ShareHashtag.Builder() //hashtag
                            .setHashtag("#DuaLipa")
                            .build()).build();
            compartirLink.setShareContent(content);

            //COMPARTIR FOTO
            //Obtener el bitmap de la imagen a compartir
            Bitmap image = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.foto);
            //Crear la foto (SharePhoto) que se va a compartir con el bitmap obtenido
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .build();
            //Agregar la SharePhoto al objeto SharePhotoContent
            SharePhotoContent contentPhoto = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            compartirFoto.setShareContent(contentPhoto);

        } else {
            Log.d("AGUS", "Usuario sin sesion");
        }

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "Autenticacion con exito", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Autenticacion cancelada", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(MainActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
                Log.d("AGUS", exception.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("AGUS", "ON ACTIVITY RESULT");
    }
}