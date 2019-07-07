window.onload = function(){
    // Get the musicals.
    doAjax("_json/musicals.json", function(_json_ar){
        for(var item of _json_ar){
            let {name,
                poster} = item;
            
            let myMusical = new Musical(id_parent, id_extansion, name, poster);
            myMusical.addToHtml();
        }
    });
}

