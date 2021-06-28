// const imgArrElem = document.querySelector('#imgArr');
const selectImgArrElem = document.querySelector('#selectImgArr');
const ctntElem = document.querySelector('#ctnt');
const btnUploadElem = document.querySelector('#btnUpload');
const displayImgsListElem = document.querySelector('#displayImgsList');

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
    displayImgsListElem.innerHTML = '';
    for(let i = 0; i < fileList.length; i++) {
        const item = fileList[i];
        const reader = new FileReader();
        reader.readAsDataURL(item);

        // 로드 한 후(이벤트)
        reader.onload = () => {
            const img = document.createElement('img');
            img.addEventListener('click', () => {
                fileList.splice(i, 1); // 몇번째 부터 몇번째 까지 잘라내 버린다.(파괴)
                displaySelectedImgArr();
            })
            img.src = reader.result;
            displayImgsListElem.append(img);
        }
    }
}

//submit 버튼 클릭시(Ajax 로 파일 업로드)
btnUploadElem.addEventListener('click', () => {
    const ctnt = ctntElem.value;
    
})