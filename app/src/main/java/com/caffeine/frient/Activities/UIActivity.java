package com.caffeine.frient.Activities;

import static com.google.android.material.badge.BadgeDrawable.BOTTOM_START;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caffeine.frient.Fragments.ActiveFragment;
import com.caffeine.frient.Fragments.FriendsFragment;
import com.caffeine.frient.Fragments.HomeFragment;
import com.caffeine.frient.Fragments.MenuFragment;
import com.caffeine.frient.Fragments.NotificationFragment;
import com.caffeine.frient.Helper.ShowProgressBar;
import com.caffeine.frient.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UIActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Dialog dialog;
    private String DP = "";
    private int width;

    private StorageReference proPicRef;
    private DatabaseReference ref;
    CircleImageView picture;
    ImageView placeholder;
    private Uri uri;
    private String URL;
    ShowProgressBar spb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiactivity);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);
        dialog = new Dialog(this);

        proPicRef  = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child("ProPics");
        ref = FirebaseDatabase.getInstance().getReference().child("Frient").child("Users").child(FirebaseAuth.getInstance().getUid());

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setIcon(R.drawable.home);
                        break;

                    case 1:
                        tab.setIcon(R.drawable.friends);
                        break;

                    case 2:
                        tab.setIcon(R.drawable.active);
                        badgeDrawable(tab, 100, 3);
                        break;

                    case 3:
                        tab.setIcon(R.drawable.notification);
                        badgeDrawable(tab, 10, 2);
                        break;

                    case 4:
                        tab.setIcon(R.drawable.menu);
                        break;
                }
            }
        });
        tabLayoutMediator.attach();

        getDPInfo();

    }

    private void badgeDrawable(TabLayout.Tab tab, int count, int max){
        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
        badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLightRed));
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(count);
        badgeDrawable.setHorizontalOffset(15);
        badgeDrawable.setVerticalOffset(8);
        badgeDrawable.setBadgeTextColor(getResources().getColor(R.color.colorWhite));
        badgeDrawable.setMaxCharacterCount(max);
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> titles = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 1:
                    return new FriendsFragment();
                case 2:
                    return new ActiveFragment();
                case 3:
                    return new NotificationFragment();
                case 4:
                    return new MenuFragment();
            }

            return new HomeFragment();
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    private void getDPInfo(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Frient").child("Users").child(FirebaseAuth.getInstance().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DP = snapshot.child("dp").getValue(String.class);

                if (DP.equals("empty")){
                    showProfilePictureDialog();
                }

                else dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showProfilePictureDialog(){
        dialog.setContentView(R.layout.upload_profile_picture);

        TextView upload;
        LinearLayout layout;

        upload = dialog.findViewById(R.id.upload);
        picture = dialog.findViewById(R.id.profile_picture);
        placeholder = dialog.findViewById(R.id.placeholder);
        layout = dialog.findViewById(R.id.profile_picture_popup_dialog_layout);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                width - 100,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        //params.setMargins(15, 0, 15, 0);
        layout.setLayoutParams(params);

        picture.setOnClickListener(v -> {
            importPicture();
        });

        upload.setOnClickListener(v -> {
            //upload profile picture
            if (uri != null){
                spb = new ShowProgressBar(this);
                spb.setContext();
                spb.show();
                uploadToCloud();
            }

            else {
                Toast.makeText(UIActivity.this, "Choose image first", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void importPicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            picture.setImageURI(uri);
            placeholder.setVisibility(View.GONE);
        }
    }

    private void uploadToCloud(){
        Bitmap bmp = null;

        try {
            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        }
        catch (Exception e){e.printStackTrace();}

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] fileInBytes = baos.toByteArray();

        final StorageReference photoRef = proPicRef.child(Long.toString(System.currentTimeMillis()));
        photoRef.putBytes(fileInBytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            URL = uri.toString();

                            ref.child("dp").setValue(URL).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        spb.dismiss();
                                        dialog.dismiss();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}