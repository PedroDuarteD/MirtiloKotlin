package com.pedroduarte.mirtiloapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pedroduarte.mirtiloapp.CustomAdapter.CustomAdapter
import com.pedroduarte.mirtiloapp.model.RecordModel
import org.json.JSONObject
import java.time.LocalDate
import java.util.Calendar
import java.util.HashMap

class MainActivity : AppCompatActivity(), CustomAdapter.OnTapScreen{

    private lateinit var list: RecyclerView
    private lateinit var btn_add: FloatingActionButton
    private lateinit var allRecords: ArrayList<RecordModel>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        list = findViewById(R.id.list)
        btn_add = findViewById(R.id.float_add)

        allRecords = ArrayList()
        btn_add.setOnClickListener {
            startActivity(Intent(applicationContext, AddRecord::class.java))
        }




    }

    override fun onResume() {
        val volley = Volley.newRequestQueue(applicationContext)
        val request = StringRequest(Request.Method.GET,Config.IPAdress+"/api/records",{
            val body = JSONObject(it).getJSONArray("records")

            (0 until body.length()).forEach {
                val record = body.getJSONObject(it)
                allRecords.add(RecordModel(record.getInt("id"),record.getString("date_apanha"), record.getString("date_entrega"), record.getInt("status"), record.getJSONObject("recetor").getString("title"), record.getJSONObject("produtor").getString("name"), record.getInt("box"), record.getString("type")))
            }

            val newadapter = CustomAdapter(allRecords,this )
            list.adapter = newadapter
            list.layoutManager = LinearLayoutManager(applicationContext)
        },{
            Toast.makeText(applicationContext,"Error",Toast.LENGTH_SHORT).show()
        })
        volley.add(request)
        super.onResume()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClickItem(position: Int) {
        val model = allRecords.get(position)

        if(model.state==0){



        val alert = AlertDialog.Builder(this)
        alert.setTitle("Update Status")
        alert.setMessage("Está entregue ?")
        alert.setPositiveButton("Sim",{ dialog, other ->

                val date = Calendar.getInstance()
            val data = date.get(Calendar.DAY_OF_MONTH).toString()+"/"+(date.get(Calendar.MONTH)+1).toString()+"/"+date.get(Calendar.YEAR).toString()

            val v = Volley.newRequestQueue(this)
            val request =object: StringRequest(Request.Method.POST,Config.IPAdress+"/api/update_record",{
                        val request = JSONObject(it)
                if(request.getString("ans")=="Success"){
                    onResume()
                }else{
                    Toast.makeText(applicationContext,request.getString("sms"),Toast.LENGTH_SHORT).show()

                }
            },{
                Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT).show()

            }){
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>();


                    params.put("pass","")
                    params.put("date",data)
                    params.put("id",model.id.toString())
                    return params
                }
            }
            v.add(request)
        })

        alert.setNegativeButton("Não",{ dialog, other ->

        })
        alert.show()
        }

    }
}