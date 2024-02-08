package kr.co.lion.android_homework_1

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.android_homework_1.databinding.ActivityMainBinding
import kr.co.lion.android_homework_1.databinding.RowBinding

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var activityInputLauncher: ActivityResultLauncher<Intent>
    lateinit var activityViewLauncher: ActivityResultLauncher<Intent>

    var listRow = mutableListOf<Memo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        initData()
        initToolbar()
        initView()
        setEvent()
    }

    fun initData(){
        val contractInput = ActivityResultContracts.StartActivityForResult()
        activityInputLauncher = registerForActivityResult(contractInput){
            if(it.resultCode == RESULT_OK){
                val info = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    it.data!!.getParcelableExtra("obj", Memo::class.java)
                }else{
                    it.data!!.getParcelableExtra<Memo>("obj")
                }
                listRow.add(info!!)
                updateRecycler()
            }
        }
        val contractView = ActivityResultContracts.StartActivityForResult()
        activityViewLauncher = registerForActivityResult(contractView){
            if(it.resultCode == RESULT_OK){ // 수정
                val info = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    it.data!!.getParcelableExtra("obj", Memo::class.java)
                }else{
                    it.data!!.getParcelableExtra<Memo>("obj")
                }
                val pos = it.data!!.getIntExtra("pos", Int.MAX_VALUE)
                listRow[pos] = info!!
                updateRecycler()
            }else if(it.resultCode == RESULT_FIRST_USER){ // 삭제
                val pos = it.data!!.getIntExtra("pos", Int.MAX_VALUE)
                listRow.removeAt(pos)
                updateRecycler()
            }
        }

    }
    fun initView(){
        activityMainBinding.apply {
            recyclerView.apply {
                adapter = RecyclerViewApdater()
                layoutManager = LinearLayoutManager(this@MainActivity)
                val deco = MaterialDividerItemDecoration(this@MainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }
    fun initToolbar(){
        activityMainBinding.apply {
            toolbarMain.apply {
                title = "메모관리"
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.itemAdd -> {
                            val intent = Intent(this@MainActivity, InputActivity::class.java)
                            activityInputLauncher.launch(intent)
                        }
                    }
                    true
                }
                inflateMenu(R.menu.main_menu)
            }
        }
    }
    fun setEvent(){

    }

    private fun updateRecycler() {
        activityMainBinding.recyclerView.adapter?.notifyDataSetChanged()
    }

    inner class RecyclerViewApdater : RecyclerView.Adapter<RecyclerViewApdater.ViewHolderClass>(){
        inner class ViewHolderClass(rowBinding: RowBinding) : RecyclerView.ViewHolder(rowBinding.root){
            val rowBinding : RowBinding
            init {
                this.rowBinding = rowBinding

                this.rowBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                this.rowBinding.root.setOnClickListener {
                    val intent = Intent(this@MainActivity, ViewActivity::class.java)
                    intent.putExtra("obj", listRow[adapterPosition])
                    intent.putExtra("pos", adapterPosition)
                    activityViewLauncher.launch(intent)
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val rowBinding = RowBinding.inflate(layoutInflater)
            val viewHolderClass = ViewHolderClass(rowBinding)
            return viewHolderClass
        }

        override fun getItemCount(): Int {
            return listRow.size
        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            holder.rowBinding.textViewTitle.text = listRow[position].title
            holder.rowBinding.textViewDate.text = listRow[position].date
        }
    }
}