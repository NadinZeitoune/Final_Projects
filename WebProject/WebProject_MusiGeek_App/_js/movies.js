window.onload = function(){
    declareExitBtn();

    // Show the movies.
    doAjax("_json/musicals.json", function(_json_ar){
        for(var item of _json_ar){
            let {name,
                prodaction,
                poster,
                movie} = item;
            let floatBox = {box: id_float, h2: id_float_h2, iframe: id_iframe};
            
            let myMovie = new Movie(id_parent, floatBox, name, prodaction, poster, movie);
            myMovie.addToHtml();
        }
    });
}

function declareExitBtn(){
    id_float.addEventListener("click",function(){
        id_float.style.display = "none";
    })
}