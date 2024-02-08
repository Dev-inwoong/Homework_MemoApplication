package kr.co.lion.android_homework_1

import android.content.Context
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlin.concurrent.thread

class Util {
    companion object {
        fun validation(view: View, memo: Memo): Boolean {
            if(memo.title!!.isBlank()){
                Snackbar.make(view, "제목을 입력해주십시오.", Snackbar.LENGTH_SHORT).show()
                return false
            } else if(memo.content!!.isBlank()){
                Snackbar.make(view, "내용을 입력해주십시오.", Snackbar.LENGTH_SHORT).show()
                return false
            }
            else {
                return memo.content!!.isNotBlank()
            }
        }
        fun showSoftInput(view: View, context: Context){
            // 포커스를 준다.
            view.requestFocus()
            thread {
                SystemClock.sleep(1000)
                val inputMethodManager = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(view, 0)
            }
        }
        fun hideSoftInput(activity: AppCompatActivity){
            // 현재 표커스를 가지고 있는 View 있다면 키보드를 내린다.
            if(activity.window.currentFocus != null){
                val inputMethodManager = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(activity.window.currentFocus?.windowToken, 0);
            }
        }
    }
}