window.onload = function(){
    
}

// Responsable for the menu burger click.
function showMenu(){
    let index = id_menu.className.indexOf(" d-block");
    console.log(index);
    if(index < 0){
        id_menu.className += " d-block";
        console.log(id_menu.className);
    }else{
        id_menu.className = id_menu.className.replace(" d-block", "");
        console.log(id_menu.className);
    }
}

function doAjax(_link, _func){
    let xmlHttp = new XMLHttpRequest();
    
    xmlHttp.onreadystatechange = function(){
        if(this.status == 200 && this.readyState == 4){
            var json_ar = JSON.parse(this.response);
            _func(json_ar);
        }
    }

    xmlHttp.open("GET", _link, true);
    xmlHttp.send();
}