package hr.markic.budgetmanager

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import hr.markic.budgetmanager.dao.NasaRepository
import hr.markic.budgetmanager.dao.getNasaRepository
import hr.markic.budgetmanager.model.Item
import java.lang.IllegalArgumentException

private const val AUTHORITY = "hr.markic.nasa.api.provider"
private const val PATH = "items"
private const val ITEMS = 10
private const val ITEM_ID = 20

val NASA_PROVIDER_URI = Uri.parse("content://$AUTHORITY/$PATH")

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, PATH, ITEMS)
    addURI(AUTHORITY, "$PATH/#", ITEM_ID)
    this
}

class NasaProvider : ContentProvider() {

    private lateinit var nasaRepository: NasaRepository

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(URI_MATCHER.match(uri)) {
            ITEMS -> return nasaRepository.delete(selection, selectionArgs)
            ITEM_ID -> {
                uri.lastPathSegment?.let {
                    return nasaRepository.delete("${Item::_id.name}=?", arrayOf(it))
                }
            }
        }
        throw IllegalArgumentException("Wrong uri")
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = nasaRepository.insert(values)
        return ContentUris.withAppendedId(NASA_PROVIDER_URI, id)
    }

    override fun onCreate(): Boolean {
        nasaRepository = getNasaRepository(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor?  = nasaRepository.query(projection, selection, selectionArgs, sortOrder)

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when(URI_MATCHER.match(uri)) {
            ITEMS -> return nasaRepository.update(values, selection, selectionArgs)
            ITEM_ID -> {
                uri.lastPathSegment?.let {
                    return nasaRepository.update(values, "${Item::_id.name}=?", arrayOf(it))
                }
            }
        }
        throw IllegalArgumentException("Wrong uri")
    }
}