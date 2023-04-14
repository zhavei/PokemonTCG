package com.syafei.pokemontcg.ui.details

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.syafei.pokemontcg.R
import com.syafei.pokemontcg.coredata.model.PokemonData
import com.syafei.pokemontcg.databinding.ActivityDetailsBinding
import com.syafei.pokemontcg.helper.CostumDialog
import com.syafei.pokemontcg.helper.Utils
import com.syafei.pokemontcg.helper.ViewModelFactory

class DetailsActivity : AppCompatActivity() {


    private val binding by lazy {
        ActivityDetailsBinding.inflate(layoutInflater)
    }
    private val detailsViewModel: DetailsViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    private var pokemonName = ""
    private var pokemonImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pokemonCard = getPokemonCard()

        setSelectedCard(pokemonCard)
        setupViewC(pokemonCard)
        setPokemonImageView()
        setOnDownloadPermitted()

    }

    private fun setOnDownloadPermitted() {

        binding.btnDownload.setOnClickListener {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) -> downloadingImage()

                else -> requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }

        }

    }

    private fun setPokemonImageView() {
        try {
            Glide.with(applicationContext)
                .load(pokemonImage).apply(
                    RequestOptions.placeholderOf(
                        R.drawable.ic_baseline_refresh_24
                    ).error(R.drawable.ic_baseline_broken_image_24)
                )
                .into(binding.ivDetails)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupViewC(pokemonCard: PokemonData?) {
        pokemonCard?.let {
            with(binding) {
                tvNameDetail.text = it.name
                if (it.hp == null) tvHpDetail.text = "0" else tvHpDetail.text = it.hp
                if (it.level == null) tvLevelDetail.text = "0" else tvLevelDetail.text = it.level
                supportActionBar?.title = it.name
            }
        }
    }

    private fun setSelectedCard(pokemonCard: PokemonData?) {
        pokemonCard?.let { pokemon ->
            pokemon.name?.let { name ->
                pokemonName = name

            }

            pokemon.images?.let { image ->
                pokemonImage = image.large
            }
        }
    }

    private fun getPokemonCard() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(POKEMON_EXTRA, PokemonData::class.java)
    } else {
        intent.getParcelableExtra(POKEMON_EXTRA)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            downloadingImage()
        } else {
            Toast.makeText(
                applicationContext,
                "You have to grant the permission to save the card's picture.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun downloadingImage() {

        val costumeDialog = CostumDialog(this)
        Utils.saveToFile(pokemonImage, pokemonName, this, onLoadingListener = {
            runOnUiThread {
                costumeDialog.showDownloadDialog(this)
            }

        }) {
            runOnUiThread {
                costumeDialog.dismissDownloadDialog()
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

    }


    companion object {
        const val POKEMON_EXTRA = "details_Poke"
    }
}