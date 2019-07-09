window.onload = function(){
    declareExitBtns();

    // Get the musicals.
    doAjax("_json/musicals.json", function(_json_ar){
        for(var item of _json_ar){
            let {name,
                poster,
                prodaction,
                movie,
                songs} = item;
    
            let movieBox = {box: id_float_movie, h2: id_float_h2_movie, iframe: id_iframe_movie};
            let songBox = {box: id_float_song, h2: id_float_h2_song, div: id_lyrics_song, audio: id_audio_song};

            let myMusical = new Musical(id_parent, id_extansion, movieBox, songBox, name, poster, prodaction, movie, songs);
            myMusical.addToHtml();
        }
    });
}

function declareExitBtns(){
    // Movie exit.
    id_float_movie.addEventListener("click",function(){
        id_float_movie.style.display = "none";
    })

    // Song exit.
    id_float_song.addEventListener("click",function(){
        id_float_song.style.display = "none";
        id_audio_song.src = "";
    })
}

