package com.oztasibrahimomer.instagramkotlin2.activites

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.oztasibrahimomer.instagramkotlin2.databinding.ActivityUploadBinding
import java.util.UUID

class UploadActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUploadBinding

    private lateinit var permissionLauncher:ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var fireStore: FirebaseFirestore
    private lateinit var myAuth:FirebaseAuth
    private lateinit var storage: FirebaseStorage
    var imageDataUri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        fireStore= Firebase.firestore
        myAuth=Firebase.auth
        storage=Firebase.storage




        registerLauncher()
    }

    @SuppressLint("SuspiciousIndentation")
    fun upload(view: View){

        val uuid=UUID.randomUUID()

        val referenceName="images/${uuid}.jpg"
        val storageReference=storage.reference



        if(imageDataUri!=null){

            val reference=storageReference.child(referenceName)

            reference.putFile(imageDataUri!!).addOnSuccessListener {

                // upload file in storage!!!



                reference.downloadUrl.addOnSuccessListener {uri->

                val hashMap= hashMapOf<String,Any>()

                    hashMap.put("email",myAuth.currentUser!!.email.toString())
                    hashMap.put("downUrl",uri)
                    hashMap.put("comment",binding.commentText.text.toString())
                    hashMap.put("date",com.google.firebase.Timestamp.now())

                    fireStore.collection("Posts").add(hashMap).addOnSuccessListener {
                        finish()


                    }

                }.addOnFailureListener {

                    Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                }




            }.addOnFailureListener {

                Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }





    }
    fun selectedImage(view:View){

        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            //request permission
            if(ActivityCompat.shouldShowRequestPermissionRationale(this@UploadActivity,android.Manifest.permission.READ_EXTERNAL_STORAGE)){

                Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give permission"){

                    //request permission
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

                }.show()
            }
            else{
                //first request permission
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

            }

        }
        else{

            //permission granted and go to gallery!!!

            val intentToGallery=Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)

        }

    }


    private fun registerLauncher(){

        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->

            if(result){

                //permission granted!!
                val intentToGallery=Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
            else{
                // permission denied!!!
                Toast.makeText(this@UploadActivity,"permission denied",Toast.LENGTH_LONG).show()
            }

        }

        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->

            if(result.resultCode== RESULT_OK){

                val intentFromGallery=result.data

                if(intentFromGallery!=null){
                    imageDataUri=intentFromGallery.data  // bu uri resmimin galerideki konuumu yolu

                    if(imageDataUri!=null){

                        binding.imageView.setImageURI(imageDataUri)
                    }
                }
            }



        }

    }
}