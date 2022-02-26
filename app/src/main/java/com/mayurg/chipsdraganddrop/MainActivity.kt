package com.mayurg.chipsdraganddrop

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mayurg.chipsdraganddrop.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val dragMessage = "Chip Added"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val names = mutableListOf("Name 1", "Name 2", "Name 3")

        for (name in names) {
            val chip = Chip(this, null, 0)
            chip.text = name
            binding.chipGroup1.addView(chip)
        }

        attachChipDragListener()

        binding.chipGroup1.setOnDragListener(chipDragListener)
    }

    private val chipDragListener = View.OnDragListener { view, dragEvent ->
        val draggableItem = dragEvent.localState as Chip

        when (dragEvent.action) {

            DragEvent.ACTION_DRAG_STARTED -> {
                true
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                true
            }

            DragEvent.ACTION_DRAG_LOCATION -> {
                true
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                //when view exits drop-area without dropping set view visibility to VISIBLE
                draggableItem.visibility = View.VISIBLE
                view.invalidate()
                true
            }

            DragEvent.ACTION_DROP -> {

                //on drop event in the target drop area, read the data and
                // re-position the view in it's new location
                if (dragEvent.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    val draggedData = dragEvent.clipData.getItemAt(0).text
                    println("draggedData $draggedData")
                }


                //on drop event remove the view from parent viewGroup
                if (draggableItem.parent != null) {
                    val parent = draggableItem.parent as ChipGroup
                    parent.removeView(draggableItem)
                }

                // get the position to insert at
                var pos = -1

                for (i in 0 until binding.chipGroup1.childCount) {
                    val chip = binding.chipGroup1[i] as Chip
                    val start = chip.x
                    val end = (chip.x + (chip.width / 2))
                    if (dragEvent.x in start..end) {
                        pos = i
                        break
                    }
                }


                //add the view view to a new viewGroup where the view was dropped
                if (pos >= 0) {
                    val dropArea = view as ChipGroup
                    dropArea.addView(draggableItem, pos)
                } else {
                    val dropArea = view as ChipGroup
                    dropArea.addView(draggableItem)
                }


                true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                draggableItem.visibility = View.VISIBLE
                view.invalidate()
                true
            }

            else -> {
                false
            }

        }
    }

    private fun attachChipDragListener() {
        for (i in 0 until binding.chipGroup1.childCount) {
            val chip = binding.chipGroup1[i]
            if (chip !is Chip)
                continue

            chip.setOnLongClickListener { view: View ->

                // Create a new ClipData.Item with custom text data
                val item = ClipData.Item(dragMessage)

                // Create a new ClipData using a predefined label, the plain text MIME type, and
                // the already-created item. This will create a new ClipDescription object within the
                // ClipData, and set its MIME type entry to "text/plain"
                val dataToDrag = ClipData(
                    dragMessage,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    item
                )

                // Instantiates the drag shadow builder.
                val chipShadow = ChipDragShadowBuilder(view)

                // Starts the drag
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    //support pre-Nougat versions
                    @Suppress("DEPRECATION")
                    view.startDrag(dataToDrag, chipShadow, view, 0)
                } else {
                    //supports Nougat and beyond
                    view.startDragAndDrop(dataToDrag, chipShadow, view, 0)
                }

                view.visibility = View.INVISIBLE
                true
            }
        }

    }


}