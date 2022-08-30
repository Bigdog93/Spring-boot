const profileImgElem = document.querySelector('#flexContainer .profile.w300.pointer');
const modalElem = document.querySelector('#profile_modal');
const modalCloseElem = document.querySelector('#profile_modal_close');
const noMainProfileList = document.querySelectorAll('.no-main-profile');
const profileImgParentList = document.querySelectorAll('.profile-img-parent');
const btnFollowElem = document.querySelector('#btnFollow');
const followerElemArr = document.querySelectorAll('.pointer.follower');
const followingElemArr = document.querySelectorAll('.pointer.following');
const followModalElem = document.querySelector('#follow_modal');
const followModalCloseElem = document.querySelector('#follow_modal_close');
const followContElem = document.querySelector('#follow_modal .followCont');
const followModalTitleElem = followModalElem.querySelector('#follow_modal_title');

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

// follow process
// type 0: follow
// type 1: unfollow
function followProc(type, iuserTo, btnElem) {
    const init = {};
    const param = { iuserTo }; // 변수명 = 값
    let queryString = '';
    switch (type) {
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
                switch (btnElem.dataset.follow) {
                    case '0': // no -> follow
                        followerCntTdElem.innerText = parseInt(followerCntTdElem.innerText) + 1;
                        btnElem.value = '팔로우 취소';
                        btnElem.className = 'instaBtn';
                        break;
                    case '1': // follow 취소
                        followerCntTdElem.innerText = parseInt(followerCntTdElem.innerText) - 1;
                        if (myJson.youFollowMe != null) {
                            btnElem.value = '맞팔로우';
                        } else {
                            btnElem.value = '팔로우';
                        }
                        btnElem.className = 'instaBtnEnable';
                        break;
                }
                btnElem.dataset.follow = 1 - btnElem.dataset.follow;
            } else {
                alert('에러발생! 팔로우 실패!');
            }
        })

}

if(btnFollowElem !== null) {
    btnFollowElem.addEventListener('click', () => {
        const param = {
            iuserTo: localConstElem.dataset.iuser
        }
        followProc(btnFollowElem.dataset.follow, param.iuserTo, btnFollowElem);
    })
};

// 팔로우 팔로잉 모달창
if(followerElemArr) {
    followerElemArr.forEach(item => {
      item.addEventListener('click', () => {
          followModalTitleElem.innerText = 'follower';
          followContElem.innerHTML = '';
          followModalElem.classList.remove('hide');

          // 프로필 사용자를 팔로우한 사람들 리스트
          fetch(`getFollowerList?iuserTo=${localConstElem.dataset.iuser}`)
              .then(res => res.json())
              .then(myJson => {
                  if(myJson.length > 0) {
                      myJson.forEach(item => {
                          const cont = makeFollowItem(item);
                          followContElem.append(cont);
                      })
                  }
              })

      })
    })
}
if(followingElemArr) {
    followingElemArr.forEach(item => {
        item.addEventListener('click', () => {
            followModalTitleElem.innerText = 'following';
            followContElem.innerHTML = '';
            followModalElem.classList.remove('hide');
            // 프로필 사용자가 팔로우한 사람들 리스트
            fetch(`getFollowList?iuserTo=${localConstElem.dataset.iuser}`)
                .then(res => res.json())
                .then(myJson => {
                    if(myJson.length > 0) {
                        myJson.forEach(item => {
                            const cont = makeFollowItem(item);
                            followContElem.append(cont);
                        })
                    }
                })
        })
    })
}
if(followModalCloseElem) {
    followModalCloseElem.addEventListener('click', () => {
        followModalElem.classList.add('hide');
    })
}

function makeFollowItem(item) {
    const globalConstElem = document.querySelector('#globalConst');
    const loginIuser = globalConstElem.dataset.iuser;

    const cont = document.createElement('div');
    cont.className = 'follow-item-cont';
    const img = document.createElement('img');
    img.className = 'profile wh40 pointer';
    img.src = `/pic/profile/${item.iuser}/${item.mainProfile}`;
    img.addEventListener('click', () => {
        moveToProfile(item.iuser); // from 'feed.js'
    })
    img.onerror = () => {
        img.style.visibility = 'hidden';
    }
    const nm = document.createElement('div');
    const nmText = document.createElement('span');
    nmText.innerText = item.nm;
    nmText.classList.add('pointer');
    nmText.addEventListener('click', () => {
        moveToProfile(item.iuser);
    })
    nm.append(nmText);

    const btn = document.createElement('input');
    btn.type = 'button';
    btn.dataset.follow = '0';
    btn.addEventListener('click', () => {
        const follow = parseInt(btn.dataset.follow);
        followProc(follow, item.iuser, btn);
    })

    cont.append(img);
    cont.append(nm);
    if(parseInt(loginIuser) !== item.iuser) { // 로그인 한 사람일때는 팔로우 안뜸
        btn.type = 'button';
        if(item.isMeFollowYou) {
            btn.dataset.follow = '1';
            btn.className = 'instaBtn pointer';
            btn.value = '팔로우 취소';
        }else {
            btn.classList.add('instaBtnEnable');
            btn.classList.add('pointer');
            btn.value = '팔로우';
        }
        cont.append(btn);
    }
    return cont;
}



const localConstElem = document.querySelector('#localConst');
feedObj.containerElem = document.querySelector('#feedContainer');
feedObj.loadingElem = document.querySelector('.loading');
feedObj.url = '/user/feedList';
feedObj.iuser = localConstElem.dataset.iuser;
feedObj.setScrollInfinity(window);
feedObj.getFeedList(1);

