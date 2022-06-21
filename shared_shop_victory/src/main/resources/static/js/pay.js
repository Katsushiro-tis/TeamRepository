    function formSwitch() {
        paynumber = document.getElementsByName('payMethod')
        if (paynumber[0].checked) {
            // クレジットが選択されたら下記を実行します
            document.getElementById('creditMenu').style.display = "";
            document.getElementById('bankMenu').style.display = "none";
            document.getElementById('tyakuMenu').style.display = "none";
            document.getElementById('elementMenu').style.display = "none";
            document.getElementById('storeMenu').style.display = "none";            
        } else if (paynumber[1].checked) {
            // 好きな場所が選択されたら下記を実行します
            document.getElementById('creditMenu').style.display = "none";
            document.getElementById('bankMenu').style.display = "";
            document.getElementById('tyakuMenu').style.display = "none";
            document.getElementById('elementMenu').style.display = "none";
            document.getElementById('storeMenu').style.display = "none";    
        } else if(paynumber[2].checked){
            document.getElementById('creditMenu').style.display = "none";
            document.getElementById('bankMenu').style.display = "none";
            document.getElementById('tyakuMenu').style.display = "";
            document.getElementById('elementMenu').style.display = "none";
            document.getElementById('storeMenu').style.display = "none";    
        } else if(paynumber[3].checked){
            document.getElementById('creditMenu').style.display = "none";
            document.getElementById('bankMenu').style.display = "none";
            document.getElementById('tyakuMenu').style.display = "none";
            document.getElementById('elementMenu').style.display = "";
            document.getElementById('storeMenu').style.display = "none";    
        } else if(paynumber[4].checked){
            document.getElementById('creditMenu').style.display = "none";
            document.getElementById('bankMenu').style.display = "none";
            document.getElementById('tyakuMenu').style.display = "none";
            document.getElementById('elementMenu').style.display = "none";
            document.getElementById('storeMenu').style.display = "";    
        }
    }
    window.addEventListener('load', formSwitch());