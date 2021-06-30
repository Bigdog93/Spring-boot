const profileImgElem = document.querySelector('#flexContainer .profile.w300.pointer');
const modalElem = document.querySelector('#profile_modal')
const modalCloseElem = document.querySelector('#profile_modal_close')
const noMainProfileList = document.querySelectorAll('.no-main-profile')
const profileImgParentList = document.querySelectorAll('.profile-img-parent')

// 모든 no-main-profile 아이콘에 이벤트 걸어주기
//이벤트는 메인 이미지 변경 처리
profileImgParentList.forEach((item) => {
    // item.addEventListener('click', () => {
    //     const iprofile = item.dataset.iprofile;
    //     console.log(iprofile);
    //     changeMainProfile(iprofile);
    // }
    const iElem = item.querySelector('i');
    if(iElem != null) {
        addIElemEvent(iElem);
    }
})

// i 태그에 이벤트 부여
function addIElemEvent(target) {
    target.addEventListener('click', () => {
        const iprofile = target.parentNode.dataset.iprofile; /* parentNode 는 부모요소에 접근. */
        console.log(iprofile);
        changeMainProfile(iprofile);
    })
}

//메인 이미지 변경
function changeMainProfile(iprofile) {
    fetch(`/user/mainProfile?iprofile=${iprofile}`)
        .then((res)=>res.json())
        .then(myJson => {
            console.log(myJson.result);
            switch (myJson.result) {
                case 0: // 이미지 변경 실패
                    alert('프로필 변경 실패 : 오류 발생');
                    break;
                case 1: // 이미지 변경 성공
                    setMainProfileIcon(iprofile); // 체크 아이콘 다시 부여
                    // section 에 있는 프로필 이미지 변경
                    const src = profileImgElem.src;
                    const frontSrc = src.substring(0, src.lastIndexOf("/"));
                    const resultSrc = frontSrc + "/" + myJson.img;
                    profileImgElem.src = resultSrc;
                    // 헤더에 있는 프로필 이미지 변경
                    const headerProfileImgElem = document.querySelector('header .span__profile img')
                    headerProfileImgElem.src = resultSrc;
                    break;
            }
        })
}

function setMainProfileIcon(iprofile) {
    profileImgParentList.forEach(item => {
        item.innerHTML = '';

        const itemIprofile = item.dataset.iprofile;
        if(iprofile !== itemIprofile) {
            const iElem = document.createElement('i');
            iElem.className = 'no-main-profile pointer fas fa-check';
            item.append(iElem);

            addIElemEvent(iElem);
        }
    });
}

// 모달창 띄우기
profileImgElem.addEventListener('click',() => { // Arrow Function
    modalElem.classList.remove('hide');
})

// 모달창 닫기
modalCloseElem.addEventListener('click', () => {
    modalElem.classList.add('hide');
    // location.reload(); // 쉬운 방법
})
feedObj.containerElem = document.querySelector('#feedContainer');
feedObj.loadingElem = document.querySelector('.loading');
feedObj.url = '/user/feedList';
feedObj.setScrollInfinity(window);
feedObj.getFeedList(1);