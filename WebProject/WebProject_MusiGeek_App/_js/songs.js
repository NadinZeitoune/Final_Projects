window.onload = function () {
    declareExitBtn();

    // Create all song collections.
    doAjax("_json/musicals.json", function (_musicals_ar) {
        for (let item of _musicals_ar) {
            let {
                name,
                songs
            } = item;

            let floatBox = {box: id_float, h2: id_float_h2, div: id_lyrics, audio: id_audio};
            createSongCollection(id_parent, floatBox, name, songs);
        }
    })
}

function declareExitBtn(){
    id_float.addEventListener("click",function(){
        id_float.style.display = "none";
        id_audio.src = "";
    })
}