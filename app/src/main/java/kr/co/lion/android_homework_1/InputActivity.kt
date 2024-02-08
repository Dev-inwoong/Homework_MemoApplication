package kr.co.lion.android_homework_1

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.android_homework_1.databinding.ActivityInputBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class InputActivity : AppCompatActivity() {
    lateinit var activityInputBinding: ActivityInputBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityInputBinding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(activityInputBinding.root)

        initData()
        initToolbar()
        initView()
        setEvent()
    }

    private fun initData() {

    }

    private fun initToolbar() {
        activityInputBinding.apply {
            toolbarInput.apply {
                title = "메모 작성"
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                inflateMenu(R.menu.input_menu)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.itemDone -> {
                            Util.hideSoftInput(this@InputActivity)
                            val title = textinputTitle.text.toString()
                            val content = textinputContent.text.toString()
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val currentDateTime = LocalDateTime.now()
                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                val formattedDateTime = currentDateTime.format(formatter)
                                val memo = Memo(title, content, formattedDateTime)
                                if(Util.validation(this, memo)){
                                    val intent = Intent(this@InputActivity, MainActivity::class.java)
                                    intent.putExtra("obj", memo)
                                    setResult(RESULT_OK, intent)
                                    finish()
                                }
                            }
                        }
                    }
                    true
                }
            }
        }
    }

    private fun initView() {
        activityInputBinding.apply {
            Util.showSoftInput(textinputTitle, this@InputActivity)
        }
    }

    private fun setEvent() {

    }
}