window.onload = function(){

    // Show the movies.
    doAjax("_json/musicals.json", function(_json_ar){
        for(var item of _json_ar){
            let {name,
                prodaction,
                poster,
                movie} = item;
            
            let myMovie = new Movie(id_parent, name, prodaction, poster, movie);
            myMovie.addToHtml();
        }
    });
}

