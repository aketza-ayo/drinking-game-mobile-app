package com.devapp.drinkinggame

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class RightFragment : Fragment() {

    private lateinit var mListener: OnFragmentInteractionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(context.toString().toString() + " must implement OnFragmentInteractionListener"
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view =  inflater.inflate(R.layout.fragment_right, container, false)

        val histoList =
            requireArguments().getParcelableArrayList<Parcelable>("historicalDeck") as MutableList<CardItem>


        val recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        var adapter = CardAdapter(histoList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                val item = adapter.getItemAt(viewHolder.adapterPosition)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                sendBack(item)
            }
        }).attachToRecyclerView(recyclerView)

        return view
    }

    private fun sendBack(cardItemDeleted: CardItem){
        if(mListener != null){
            mListener.onFragmentInteraction(cardItemDeleted)
        }
    }

    interface OnFragmentInteractionListener{

        fun onFragmentInteraction(cardItemDeleted: CardItem)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RightFragment()
    }
}
