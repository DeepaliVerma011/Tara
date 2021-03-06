package com.deepaliverma.tara

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import kotlinx.android.synthetic.main.activity_chat.*

const val UID="uid"
const val NAME="name"
const val IMAGE="photo"
class ChatActivity : AppCompatActivity() {
private val friendI: String? =intent.getStringExtra(UID)
    private val name: String? =intent.getStringExtra(NAME)
    private val image: String? =intent.getStringExtra(IMAGE)

    private val mCurrentUid:String by lazy{
        FirebaseAuth.getInstance().uid!!
    }
    private val db:FirebaseDatabase by lazy{
        FirebaseDatabase.getInstance()
    }
    lateinit var currentUser:User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(GoogleEmojiProvider())
        setContentView(R.layout.activity_chat)
FirebaseFirestore.getInstance().collection("users").document(mCurrentUid).get()
    .addOnSuccessListener {
        currentUser= it.toObject(User::class.java)!!
    }

        nameTv.text=name
        Picasso.get().load(image).into(userImgView)

    }


}