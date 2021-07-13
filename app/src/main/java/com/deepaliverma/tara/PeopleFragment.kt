package com.deepaliverma.tara

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_people.*
import java.lang.Exception

class PeopleFragment : Fragment() {

lateinit var mAdapter: FirestorePagingAdapter<User,UsersViewHolder>
val auth by lazy{
    FirebaseAuth.getInstance()
}
    val database by lazy{
        FirebaseFirestore.getInstance().collection("users")
            .orderBy("name",Query.Direction.DESCENDING)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setUpAdapter()
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    private fun setUpAdapter() {
        val config= PagedList.Config.Builder()
            .setEnablePlaceholders(false)     //empty values before loading
            .setPageSize(10)                   //how many items in one call
            .setPrefetchDistance(2)           //no of pages required to be loaded initially
            .build()

        val options= FirestorePagingOptions.Builder<User>()
            .setLifecycleOwner(viewLifecycleOwner)   //livedata
            .setQuery(database,config,User::class.java)
            .build()
mAdapter=object :FirestorePagingAdapter<User,UsersViewHolder>(options){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view=layoutInflater.inflate(R.layout.list_item,parent,false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int, model: User) {
        holder.bind(user=model)
    }

    override fun onLoadingStateChanged(state: LoadingState) {
        super.onLoadingStateChanged(state)
        when(state){
            LoadingState.ERROR->{

            }
          LoadingState.FINISHED-> {}
            LoadingState.LOADING_MORE-> {}
            LoadingState.LOADING_INITIAL-> {}
            LoadingState.LOADED-> {}
        }
    }

    override fun onError(e: Exception) {
        super.onError(e)
    }
}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply{
            layoutManager=LinearLayoutManager(requireContext())
            adapter=mAdapter
        }
    }
}