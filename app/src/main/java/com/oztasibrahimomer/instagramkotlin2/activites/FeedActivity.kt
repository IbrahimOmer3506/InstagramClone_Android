package com.oztasibrahimomer.instagramkotlin2.activites

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
//import com.oztasibrahimomer.instagramkotlin2.MainActivity
import com.oztasibrahimomer.instagramkotlin2.Post
import com.oztasibrahimomer.instagramkotlin2.R
//import com.oztasibrahimomer.instagramkotlin2.UploadActivity
import com.oztasibrahimomer.instagramkotlin2.adapter.PostAdapter
import com.oztasibrahimomer.instagramkotlin2.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFeedBinding

    private lateinit var myAuth:FirebaseAuth

    private lateinit var firestore: FirebaseFirestore

    private lateinit var postList:ArrayList<Post>
    private lateinit var postadapter:PostAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        postList=ArrayList<Post>()

        myAuth= Firebase.auth //firebaseAuth initize edildi!!!
        firestore=Firebase.firestore


        getData()


        binding.recyclerViewFeed.layoutManager=LinearLayoutManager(this)

        postadapter=PostAdapter(postList)

        binding.recyclerViewFeed.adapter=postadapter
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId== R.id.goUpload){
            val intent= Intent(this@FeedActivity, UploadActivity::class.java)
            startActivity(intent)

        }
        else if(item.itemId== R.id.sigOut){
            val intent=Intent(this@FeedActivity, MainActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            myAuth.signOut() // firebaseAuth ile açılan oturumu kapatmaya yarıyor!!!
            startActivity(intent)






        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater=MenuInflater(this)
        inflater.inflate(R.menu.feed_activity_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData(){


        // burada firestore dan verilrei çekip recycler viewde göstereceğiz!!!


        firestore.collection("Posts").orderBy("date",
            Query.Direction.DESCENDING).addSnapshotListener { value, error ->


            if(error!=null){

                Toast.makeText(this@FeedActivity,error.localizedMessage,Toast.LENGTH_LONG).show()
            }
            else{
                if(value!=null){

                    if(!value.isEmpty){

                        val documents=value.documents


                        postList.clear()

                        for(doc in documents){

                            val comment=doc.get("comment") as String
                            val email=doc.get("email") as String
                            val downUrl=doc.get("downUrl") as String


                            val post= Post(email,downUrl,comment)

                            postList.add(post)


                        }
                        postadapter.notifyDataSetChanged()





                    }


                }
            }



        }

    }
}