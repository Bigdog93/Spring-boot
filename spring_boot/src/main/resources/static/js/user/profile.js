const profileImgElem = document.querySelector('#flexContainer .profile.w300.pointer');
const modalElem = document.querySelector('#profile_modal');
const modalCloseElem = document.querySelector('#profile_modal_close');
const noMainProfileList = document.querySelectorAll('.no-main-profile');
const profileImgParentList = document.querySelectorAll('.profile-img-parent');
const btnFollowElem = document.querySelector('#btnFollow');

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
if(btnFollowElem !== null) {
    btnFollowElem.addEventListener('click', () => {
        const param = {
            iuserTo: localConstElem.dataset.iuser
        }
        const init = {}; // 상수(const)는 주소값을 못바꿀 뿐, 안의 값은 바꿀 수 있다.
        let queryString = '';
        switch (btnFollowElem.dataset.follow) { // dataset 은 다 문자열로 넘어온다.
            case '0': // noFollow -> follow
                init.method = 'POST';
                init.headers = {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json;charset=utf-8'
                };
                init.body = JSON.stringify(param);
                break;
            case '1': // follow -> noFollow
                init.method = 'DELETE';
                queryString = `?iuserTo=${param.iuserTo}`;
                break;
        }
        fetch('follow' + queryString, init)
            .then(res => res.json())
            .then(myJson => {
                if (myJson.result == 1) {
                    const followerCntTdElem = document.querySelector('#followerCntTd');
                    switch (btnFollowElem.dataset.follow) {
                        case '0': // no -> follow
                            followerCntTdElem.innerText = parseInt(followerCntTdElem.innerText) + 1;
                            btnFollowElem.value = '팔로우 취소';
                            btnFollowElem.className = 'instaBtn';
                            break;
                        case '1': // follow 취소
                            followerCntTdElem.innerText = parseInt(followerCntTdElem.innerText) - 1;
                            if (myJson.youFollowMe != null) {
                                btnFollowElem.value = '맞팔로우';
                            } else {
                                btnFollowElem.value = '팔로우';
                            }
                            btnFollowElem.className = 'instaBtnEnable';
                            break;
                    }
                    btnFollowElem.dataset.follow = 1 - btnFollowElem.dataset.follow;
                } else {
                    alert('에러발생! 팔로우 실패!');
                }
            })
    })
};



const localConstElem = document.querySelector('#localConst');
feedObj.containerElem = document.querySelector('#feedContainer');
feedObj.loadingElem = document.querySelector('.loading');
feedObj.url = '/user/feedList';
feedObj.iuser = localConstElem.dataset.iuser;
feedObj.setScrollInfinity(window);
feedObj.getFeedList(1);

