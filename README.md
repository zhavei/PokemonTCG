This Android application utilizes the Pokemon TCG Public API (https://docs.pokemontcg.io/) to display a list of Pokemon cards and their details. The application has two pages with customizable designs. The following are the specifications of each page:

Pokemon Card List:
Displays small resolution images and names of Pokemon cards.
Uses pagination with 20 data per page.
Allows for searching.
Can view data offline after initial online retrieval.

Pokemon Card Detail:
Displays at least one high resolution image, name, and type of a selected Pokemon card.
Allows for downloading of compressed images.
This application uses Retrofit 2, Paging, Recyclerview + ListAdapter, and Glide libraries.
