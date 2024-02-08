package kr.co.lion.android_homework_1

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kr.co.lion.android_homework_1.databinding.ActivityViewBinding

class ViewActivity : AppCompatActivity() {
    lateinit var activityViewBinding: ActivityViewBinding
    lateinit var activityEditLauncher: ActivityResultLauncher<Intent>

    var memo : Memo? = null
    var pos : Int? = null
    var editMemo : Memo? = null
    var isEdited = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewBinding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(activityViewBinding.root)

        initData()
        initToolbar()
        initView()
        setEvent()
    }

    private fun initData() {
        memo = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra("obj", Memo::class.java)
        }else{
            intent.getParcelableExtra<Memo>("obj")
        }
        pos = intent.getIntExtra("pos", Int.MAX_VALUE)

        val contractEdit = ActivityResultContracts.StartActivityForResult()
        activityEditLauncher = registerForActivityResult(contractEdit){
            if(it.resultCode == RESULT_OK){
                memo = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    it.data!!.getParcelableExtra("obj", Memo::class.java)
                }else{
                    it.data!!.getParcelableExtra<Memo>("obj")
                }
                editMemo = memo
                isEdited = true
                initView()
            }
        }
    }

    private fun initToolbar() {
        activityViewBinding.apply {
            toolbarView.apply {
                title = "메모 보기"
                inflateMenu(R.menu.view_menu)
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    if(isEdited){
                        val intent = Intent(this@ViewActivity, MainActivity::class.java)
                        intent.putExtra("obj", editMemo)
                        intent.putExtra("pos", pos)
                        setResult(RESULT_OK, intent)
                        finish()
                    }else{
                        setResult(RESULT_CANCELED)
                        finish()
                    }

                }
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.itemEdit -> {
                            val intent = Intent(this@ViewActivity, EditActivity::class.java)
                            intent.putExtra("obj", memo)
                            activityEditLauncher.launch(intent)
                        }
                        R.id.itemDelete -> {
                            val intent = Intent(this@ViewActivity, MainActivity::class.java)
                            intent.putExtra("pos", pos)
                            setResult(RESULT_FIRST_USER, intent)
                            finish()
                        }
                    }
                    true
                }
            }
        }
    }

    private fun initView() {
        activityViewBinding.apply {
            textinputTitle.setText(memo!!.title)
            textinputTitle.isEnabled = false
            textinputDate.setText(memo!!.date)
            textinputDate.isEnabled = false
            textinputContent.setText(memo!!.content)
            textinputContent.isEnabled = false
        }
    }

    private fun setEvent() {

    }
}