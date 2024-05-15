package com.example.newappcdamut

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NoteDetailActivity : AppCompatActivity() {

    companion object {
        val REQUEST_EDIT_NOTE = 1
        val EXTRA_NOTE = "note"
        val EXTRA_NOTE_INDEX = "notreIndex"
        val ACTION_SAVE_NOTE = "ACTION_SAVE_NOTE"
        val ACTION_DELETE_NOTE = "ACTION_DELETE_NOTE"

        lateinit var note: Note
        var noteIndex: Int = -1

        lateinit var titleView: TextView
        lateinit var textView: TextView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_note_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        note = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_NOTE, Note::class.java)!!
        } else {
            intent.getParcelableExtra<Note>(EXTRA_NOTE)!!
        }

        noteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, -1)

        titleView = findViewById<TextView>(R.id.title)
        textView = findViewById<TextView>(R.id.text)

        titleView.text = note.title
        textView.text = note.text
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_note_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveNote()
                return true
            }
            R.id.action_delete -> {
                showConfirmDeleteDialog()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmDeleteDialog(){
        val confirmFragment = note.title?.let { ConfirmDeleteNoteFragment(it) }
        confirmFragment?.listener = object : ConfirmDeleteNoteFragment.ConfirmDeleteDialogListener{
            override fun onDialogPositiveClick() {
                TODO("Not yet implemented")
            }

            override fun onDialogNegativeClick() {}
        }
        confirmFragment?.show(supportFragmentManager, "confirmDeleteDialog")
    }

    fun saveNote(){
        note.title = titleView.text.toString()
        note.text = textView.text.toString()
        intent = Intent(ACTION_SAVE_NOTE)
        intent.putExtra(EXTRA_NOTE, note)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun deleteNote(){
        intent = Intent(ACTION_DELETE_NOTE)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}