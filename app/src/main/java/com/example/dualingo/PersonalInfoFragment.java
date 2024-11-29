package com.example.dualingo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dualingo.Models.User;
import com.example.dualingo.databinding.FragmentPersonalInfoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class PersonalInfoFragment extends Fragment {

    private String currentUserId;
    private Uri imageUri;
    private List<String> followerList = new ArrayList<>();
    private List<String> followingList = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private FragmentPersonalInfoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupImagePicker(); // Set up the image picker launcher

        binding.btnDeleteAccount.setOnClickListener(view1 -> logout());
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getProfileDataFromFirebase(currentUserId);

        // Click listener for profile image to open image picker
        binding.ivAvatar.setOnClickListener(v -> checkPermissionAndOpenImagePicker());

        binding.btnAddFriend.setOnClickListener(v -> {
            AddFriendDialogFragment dialog = AddFriendDialogFragment.newInstance(true, new ArrayList<>());
            dialog.show(getParentFragmentManager(), "AddFriendDialog");
        });

        binding.btnFollowers.setOnClickListener(v -> {
            if (followerList != null && !followerList.isEmpty()) {
                getUsersFromList(followerList);
            }
        });

        binding.btnFollows.setOnClickListener(v -> {
            if (followingList != null && !followingList.isEmpty()) {
                getUsersFromList(followingList);
            }
        });

        return view;
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        Glide.with(this).load(imageUri).into(binding.ivAvatar);
                        uploadImage();
                    }
                }
        );
    }

    private void checkPermissionAndOpenImagePicker() {
        String permission = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) ?
                android.Manifest.permission.READ_MEDIA_IMAGES :
                Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            openImagePicker();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, 100);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void uploadImage() {
        if (imageUri != null) {
            StorageReference imgRef = FirebaseStorage.getInstance()
                    .getReference()
                    .child("images/" + currentUserId);

            UploadTask uploadTask = imgRef.putFile(imageUri);
            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("ProfilePage", "Task completed successfully");
                } else {
                    Log.e("ProfilePage", "Task failed with exception: " + task.getException());
                }
            });

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    updateProfilePicture(downloadUrl);
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfilePicture(String downloadUrl) {
        FirebaseFirestore.getInstance().collection("users")
                .document(currentUserId)
                .update("profilePic", downloadUrl)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Profile picture updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to update profile picture", Toast.LENGTH_SHORT).show();
                });
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void getProfileDataFromFirebase(String userId) {
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Lấy dữ liệu từ document và cập nhật UI
                        String username = documentSnapshot.getString("username");
                        followerList = (List<String>) documentSnapshot.get("followerList");
                        followingList = (List<String>) documentSnapshot.get("followingList");

                        Long exp = (Long) documentSnapshot.get("exp");
                        binding.tvXP.setText(String.valueOf(exp));

                        Long rankId = (Long) documentSnapshot.get("rank");

                        // Lấy tên của rank từ Firestore theo rankId
                        if (String.valueOf(rankId) != null) {
                            db.collection("rank")
                                    .document(String.valueOf(rankId))
                                    .get()
                                    .addOnSuccessListener(rankDocument -> {
                                        if (rankDocument.exists()) {
                                            String rankName = rankDocument.getString("name");
                                            binding.tvRanking.setText(rankName);
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // Xử lý lỗi khi không lấy được document
                                        Toast.makeText(getContext(), "Failed to fetch rank", Toast.LENGTH_SHORT).show();
                                    });
                        }

                        // Lấy thông tin streak
                        Long streak = (Long) documentSnapshot.get("streak");
                        binding.tvStreak.setText("Streak: #" + streak);

                        // Lấy thông tin progress
                        User.Progress progress = documentSnapshot.get("progress", User.Progress.class); // Sử dụng đối tượng Progress
                        if (progress != null) {
                            // Hiển thị thông tin progress
                            binding.tvProgress.setText("Session: " + progress.getSessionIndex() + ", Lecture: " + progress.getLectureIndex());
                        }

                        String followerCount = followerList != null ? String.valueOf("FOLLOWER: #"+followerList.size()) : "FOLLOWER: #0";
                        String followingCount = followingList != null ? String.valueOf("FOLLOWS: #"+followingList.size()) : "FOLLOWS: #0";

                        binding.tvUsername.setText(username);

                        String profilePicUrl = (String) documentSnapshot.get("profilePic");
                        Glide.with(this)
                                .load(profilePicUrl)
                                .into(binding.ivAvatar);
                        binding.btnFollowers.setText(followerCount);
                        binding.btnFollows.setText(followingCount);
                    }
                })
                .addOnFailureListener(e -> e.printStackTrace());
    }

    private void getUsersFromList(List<String> list) {
        List<User> listUsers = new ArrayList<>();
        for (String userId : list) {
            db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    listUsers.add(user);

                    // Display dialog once all users are loaded
                    if (listUsers.size() == list.size()) {
                        showDialog(listUsers);
                    }
                }
            }).addOnFailureListener(e -> {
                Log.e("Firebase", "Failed to fetch user: " + userId, e);
            });
        }
    }

    private void showDialog(List<User> followerUsers) {
        AddFriendDialogFragment dialog = AddFriendDialogFragment.newInstance(false, followerUsers);
        dialog.show(getParentFragmentManager(), "AddFriendDialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up the binding
        binding = null;
    }
}
