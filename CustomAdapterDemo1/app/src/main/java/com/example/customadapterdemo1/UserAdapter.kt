package com.example.customadapterdemo1
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class UsersAdapter(context: Context, users: ArrayList<User>) : ArrayAdapter<User>(context, 0, users) {

    private class ViewHolder {
        lateinit var name: TextView
        lateinit var home: TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            viewHolder = ViewHolder()
            view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
            viewHolder.name = view.findViewById(R.id.tvName)
            viewHolder.home = view.findViewById(R.id.tvHome)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val user = getItem(position)
        viewHolder.name.text = user?.name
        viewHolder.home.text = user?.hometown

        return view!!
    }
}