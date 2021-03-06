package com.deepaliverma.tara

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_people.*
import java.lang.Exception

private const val DELETED_VIEW_TYPE = 1
private const val NORMAL_VIEW_TYPE = 2

class PeopleFragment : Fragment() {

    lateinit var mAdapter: FirestorePagingAdapter<User, RecyclerView.ViewHolder>
    val auth by lazy {
        FirebaseAuth.getInstance()
    }
    val database by lazy {
        FirebaseFirestore.getInstance().collection("users")
            .orderBy("name", Query.Direction.ASCENDING)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setUpAdapter()
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    private fun setUpAdapter() {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)     //empty values before loading
            .setPageSize(10)                   //how many items in one call
            .setPrefetchDistance(2)           //no of pages required to be loaded initially
            .build()

        val options = FirestorePagingOptions.Builder<User>()
            .setLifecycleOwner(viewLifecycleOwner)   //livedata
            .setQuery(database, config, User::class.java)
            .build()
        mAdapter = object : FirestorePagingAdapter<User, RecyclerView.ViewHolder>(options) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                return when (viewType) {
                    NORMAL_VIEW_TYPE -> UsersViewHolder(
                        layoutInflater.inflate(
                            R.layout.list_item,
                            parent,
                            false
                        )
                    )
                    else -> EmptyViewHolder(
                        layoutInflater.inflate(
                            R.layout.empty_view,
                            parent,
                            false
                        )
                    )

                }
            }

            override fun onBindViewHolder(
                holder: RecyclerView.ViewHolder,
                position: Int,
                model: User
            ) {
             if(holder is UsersViewHolder){
              holder.bind(user=model){name:String,photo:String,id:String->
                  val intent = Intent(requireContext(),ChatActivity::class.java)
                  intent.putExtra(UID,id)
                  intent.putExtra(NAME,name)
                  intent.putExtra(IMAGE,photo)
                  startActivity(intent)
              }
             }
                else{

                }
            }

            override fun onLoadingStateChanged(state: LoadingState) {
                super.onLoadingStateChanged(state)
                when (state) {
                    LoadingState.ERROR -> {

                    }
                    LoadingState.FINISHED -> {
                    }
                    LoadingState.LOADING_MORE -> {
                    }
                    LoadingState.LOADING_INITIAL -> {
                    }
                    LoadingState.LOADED -> {
                    }
                }
            }

            override fun onError(e: Exception) {
                super.onError(e)
            }

            override fun getItemViewType(position: Int): Int {
                val item = getItem(position)?.toObject(User::class.java)
                return if (auth.uid == item!!.uid) {
                    DELETED_VIEW_TYPE
                } else {
                    NORMAL_VIEW_TYPE
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }
}