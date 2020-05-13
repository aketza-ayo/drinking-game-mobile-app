package com.devapp.drinkinggame

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


private const val ARG_PARAM1 = "param1"

class RightFragment : Fragment() {
    private var param1: String? = null

    private lateinit var ediTextFragment: EditText
    private lateinit var buttonFragment: Button

    private lateinit var mListener: OnFragmentInteractionListener
    private val exampleList = generateDummyList(20)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
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

//        buttonFragment = view.findViewById<Button>(R.id.button_fragment)
//        ediTextFragment = view.findViewById<EditText>(R.id.edit_fragment)
//        ediTextFragment.setText(param1)
//        ediTextFragment.requestFocus()

//        buttonFragment.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View) {
//                var sendBackText = ediTextFragment.text.toString()
//                sendBack(sendBackText)
//            }
//        })


        val recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        var adapter = CardAdapter(exampleList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {

                val item = adapter.getItemAt(viewHolder.adapterPosition)
                exampleList.remove(item)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                sendBack(item.text1 + " deleted")

                // might need to print the list again ?
//                Toast.makeText(this@BlankFragment, "Card restored", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerView)

        return view
    }

    private fun sendBack(sendBackText: String){
        if(mListener != null){
            mListener.onFragmentInteraction(sendBackText)
        }

    }

    interface OnFragmentInteractionListener{

        fun onFragmentInteraction(sendBackText: String)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            RightFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    private fun generateDummyList(size: Int): MutableList<CardItem> {
        val list = mutableListOf<CardItem>()
        for (i in 0 until size) {
            val drawable = when (i % 3) {
                0 -> R.drawable.ic_2c_small
                1 -> R.drawable.ic_3d_small
                else -> R.drawable.ic_4s_small
            }
            val item = CardItem(drawable, "Item $i", "Line 2")
            list += item
        }

        return list
    }
}
