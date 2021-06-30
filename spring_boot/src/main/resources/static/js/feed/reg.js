// const imgArrElem = document.querySelector('#imgArr');
const selectImgArrElem = document.querySelector('#selectImgArr');
const locationElem = document.querySelector('#location');
const ctntElem = document.querySelector('#ctnt');
const btnUploadElem = document.querySelector('#btnUpload');
const displayImgsListElem = document.querySelector('#displayImgsList');

//글 내용 변경시
ctntElem.addEventListener('keyup', () => {
    toggleBtnUpload();
})


// imgArrElem.addEventListener('change', () => {
//     selectedImgsListElem.innerHTML = '';
//     // input type="file" 안의 파일 가져오기(무조건 배열로 가져온다. multiple 없으면 1칸자리 배열
//     const fileList = imgArrElem.files;
//     for(let i = 0; i < fileList.length; i++) {
//         const item = fileList[i];
//         const reader = new FileReader(); // 파일을 읽어주는 객체
//         reader.readAsDataURL(item); // 데이터의 URL 이 뭔지 읽어줭
//
//         // 로드 한 후(이벤트)
//         reader.onload = () => {
//             const img = document.createElement('img');
//             img.src = reader.result;
//             selectedImgsListElem.append(img);
//         }
//     }
// });

const fileList = [];
// 배열 합치기
// let arr = [...arr1, ...arr2, ...arr3]; 이런식으로 가능 (... : 전개연산자)

// 이미지들이 선택되면 fileList 에 추가하기
selectImgArrElem.addEventListener('change', () => {
    const files = selectImgArrElem.files;
    for(let i = 0; i < files.length; i++) {
        fileList.push(files[i])
    }
    // input type="file" 안의 파일 가져오기(무조건 배열로 가져온다. multiple 없으면 1칸자리 배열
    displaySelectedImgArr();
});

// 선택한 이미지들 디스플레이 하기
function displaySelectedImgArr() {
    toggleBtnUpload();
    displayImgsListElem.innerHTML = '';
    for(let i = 0; i < fileList.length; i++) {
        const item = fileList[i];
        const reader = new FileReader(); // 자바로 칠면 new file(); 이다.
        reader.readAsDataURL(item); // 얘는 비동기 처리

        // 로드가 끝나면(이벤트)
        reader.onload = () => {
            const img = document.createElement('img');
            img.addEventListener('click', () => { // 그 이미지에 addEventListener
                fileList.splice(i, 1); // 몇번째 부터 몇번째 까지 잘라내 버린다.(파괴)
                displaySelectedImgArr();
            })
            img.src = reader.result; // 파일의 로컬 주소값( + bite 값..?)
            displayImgsListElem.append(img);
        }
    }
}

// submit 버튼 활성, 비활성
function toggleBtnUpload() {
    // btnUploadElem.disabled = true;
    // if(ctntElem.value.length > 0 || fileList.length > 0) {
    //     btnUploadElem.disabled = false;
    // }
    btnUploadElem.disabled = !(ctntElem.value.length > 0 || fileList.length > 0);
}


//submit 버튼 클릭시(Ajax 로 파일 업로드)
btnUploadElem.addEventListener('click', () => {
    const data = new FormData(); // 일종의 form 데이타를 만들어주는것
    if(ctntElem.value.length > 0) { data.append(ctntElem.id, ctntElem.value); }
    if(locationElem.value.length > 0) { data.append(locationElem.id, locationElem.value); }
    for(let i = 0; i < fileList.length; i++ ) {
        data.append('imgArr', fileList[i]);
    }


    fetch('/feed/reg', {
        method: 'POST',
        body: data
    }).then(res => res.json())
        .then(myJson => {
            switch(myJson.result) {
                case 0:
                    alert('피드 등록 실패 : 오류 발생');
                    break;
                case 1:
                    location.href = '/feed/home';
                    break;
            }
        })
})