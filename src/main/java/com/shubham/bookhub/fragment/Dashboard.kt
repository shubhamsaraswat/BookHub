package com.shubham.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shubham.bookhub.R
import com.shubham.bookhub.adapter.DashboardRecyclerAdapter
import com.shubham.bookhub.model.Book
import com.shubham.bookhub.util.ConnectionManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.json.JSONException

class Dashboard : Fragment() {
    lateinit var recyclerdashboard: RecyclerView
    lateinit var layoutmanager:RecyclerView.LayoutManager
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar
    val bookInfoList=ArrayList<Book>()

    lateinit var  recyclerAdapter:DashboardRecyclerAdapter
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerdashboard = view.findViewById(R.id.recyclerdashboard)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)
        progressLayout.visibility=View.VISIBLE
        layoutmanager=LinearLayoutManager(activity)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_book/"
        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                try {
                    progressLayout.visibility=View.GONE
                    val success = it.getBoolean("Success")
                    if (success) {
                        val data = it.getJSONArray("Data")
                        for (i in 0 until data.length()) {
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")

                            )
                            bookInfoList.add(bookObject)
                            recyclerAdapter =
                                DashboardRecyclerAdapter(activity as Context, bookInfoList)
                            recyclerdashboard.adapter = recyclerAdapter
                            recyclerdashboard.layoutManager = layoutmanager
                        }
                    } else {
                        Toast.makeText(
                            activity as Context,
                            "Some Error Occured",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                catch(e:JSONException){
                    Toast.makeText(activity as Context,"Some Unexpected Error Occured !!!",Toast.LENGTH_SHORT).show()
                }
                println("Response is $it")
            }, Response.ErrorListener {
               Toast.makeText(activity as Context,"Volley Error Occured !!",Toast.LENGTH_SHORT ).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "Application/json"
                    headers["token"] = "58f9a05c4ec89e"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection not found")
            dialog.setPositiveButton("Open Settings") { text, Listener ->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, Listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }
        return view
    }

}