package kr.co.lion.android_homework_1

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.android_homework_1.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    lateinit var activityEditBinding: ActivityEditBinding

    var memo : Memo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEditBinding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(activityEditBinding.root)

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
    }

    private fun initToolbar() {
        activityEditBinding.apply {
            toolbarEdit.apply {
                title = "메모 수정"
                inflateMenu(R.menu.edit_menu)
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.itemDone -> {
                            Util.hideSoftInput(this@EditActivity)
                            val intent = Intent(this@EditActivity, ViewActivity::class.java)
                            val obj = getEditMemo()
                            if(Util.validation(this, obj)){
                                intent.putExtra("obj", obj)
                                setResult(RESULT_OK, intent)
                                finish()
                            }
                        }
                    }
                    true
                }
            }
        }
    }

    private fun getEditMemo(): Memo {
        activityEditBinding.apply {
            val title = textinputTitle.text.toString()
            val content = textinputContent.text.toString()
            return Memo(title, content, this@EditActivity.memo!!.date)
        }
    }

    private fun initView() {
        activityEditBinding.apply {
            textinputTitle.setText(memo!!.title)
            textinputContent.setText(memo!!.content)
        }
    }

    private fun setEvent() {
    }
}