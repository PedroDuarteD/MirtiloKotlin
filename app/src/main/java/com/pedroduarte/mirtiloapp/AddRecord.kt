package com.pedroduarte.mirtiloapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject
import java.util.Calendar
import java.util.Map
import kotlin.collections.Map as Map1

class AddRecord : AppCompatActivity() {

    private lateinit var  btn_add: Button

    private lateinit var edit_caixa: TextInputLayout
    private lateinit var edit_initial_date: TextInputLayout
    private lateinit var selectRecetor: Spinner
    private lateinit var selectType: Spinner

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_record)

        supportActionBar?.setTitle("add Mirtilos")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_add = findViewById(R.id.btn_add)
        edit_caixa = findViewById(R.id.edit_box)
        edit_initial_date = findViewById(R.id.edit_first_date)
        selectRecetor = findViewById(R.id.recetor)
        selectType = findViewById(R.id.type_mirtilo)


        val date = Calendar.getInstance()
        val data = date.get(Calendar.DAY_OF_MONTH).toString()+"/"+(date.get(Calendar.MONTH)+1).toString()+"/"+date.get(
            Calendar.YEAR).toString()

        edit_initial_date.editText!!.setText(data)

        btn_add.setOnClickListener {
            if(edit_caixa.editText!!.text.isNotEmpty() && edit_initial_date.editText!!.text.isNotEmpty() && selectRecetor.selectedItem!=null && selectType.selectedItem!=null){

                val volley = Volley.newRequestQueue(applicationContext)
                val request =object: StringRequest(Request.Method.POST,Config.IPAdress+"/api/add_record",{
                    var request = JSONObject(it)
                    if(request.getString("ans")=="Success"){
                        finish()
                    }else{
                        Toast.makeText(applicationContext,request.getString("sms"),Toast.LENGTH_SHORT).show()
                    }

                },{
                    Toast.makeText(applicationContext,"Erro no post Android",Toast.LENGTH_SHORT).show()
                }){
                    override fun getParams(): MutableMap<String, String>? {
                        val paramas= HashMap<String, String>()

                        var idRecetor = selectType.selectedItemId
                        idRecetor+=1

                        var idpro = selectRecetor.selectedItemId
                        idpro+=1

                        paramas.put("box", edit_caixa.editText!!.text.toString())
                        paramas.put("date_apanha", edit_initial_date.editText!!.text.toString())
                        paramas.put("status", "0")
                          paramas.put("pass", "")
                        paramas.put("type", idRecetor.toString())
                        paramas.put("produtor", "1")
                        paramas.put("recetor", idpro.toString())
                        return paramas
                    }
                }
                volley.add(request)
            }else{
                Toast.makeText(applicationContext, "Erro falta algo !", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}