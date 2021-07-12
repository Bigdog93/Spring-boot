const feedObj = {
    limit: 5,
    itemLength: 0,
    currentPage: 1,
    url: '',
    iuser: 0,
    swiper: null,
    containerElem: document.querySelector('#feedContainer'),
    loadingElem: document.querySelector('.loading'),
    makeFeedItem: function (item) {
        const itemContainer = document.createElement('div');
        itemContainer.classList.add('item');

        // top 영역
        let imgTag = ``;
        if(item.mainProfile != null) {
            imgTag = `<img src="/pic/profile/${item.iuser}/${item.mainProfile}" onclick="moveToProfile(${item.iuser})"
                onerror="this.style.display='none'">`
        }
        const regDtInfo = getDateTimeInfo(item.regdt);
        const topDiv = document.createElement('div');
        topDiv.classList.add('topDiv')
        topDiv.innerHTML = `
            <div class="itemProfileCont pointer">
                ${imgTag}
            </div>
            <div>
                <div class="userNmCls pointer"><span onclick="moveToProfile(${item.iuser})">${item.writer}</span></div>
                <div><span class="regdtDiv">${regDtInfo}</span>${item.location == null ? '' : (' - ' + item.location)}</div>
            </div>
        `;

        // img 영역
        const imgDiv = document.createElement('div');
        imgDiv.classList.add('imgDiv');

        const swiperContainerDiv = document.createElement('div');
        swiperContainerDiv.classList.add('swiper-container');

        const swiperWrapperDiv = document.createElement('div');
        swiperWrapperDiv.classList.add('swiper-wrapper');

        swiperContainerDiv.append(swiperWrapperDiv);
        imgDiv.append(swiperContainerDiv);

        for(let z=0; z<item.imgList.length; z++) {
            const imgObj = item.imgList[z];

            const swiperSlideDiv = document.createElement('div');
            swiperSlideDiv.classList.add('swiper-slide');

            const img = document.createElement('img');
            img.src = `/pic/feed/${item.ifeed}/${imgObj.img}`;
            swiperSlideDiv.append(img);
            swiperWrapperDiv.append(swiperSlideDiv);
        }

        itemContainer.append(topDiv);
        itemContainer.append(imgDiv);

        // fav 영역
        const favDiv = document.createElement('div');
        favDiv.classList.add('favDiv');
        const heartIcon = document.createElement('i');
        heartIcon.classList.add('fa-heart');
        heartIcon.classList.add('pointer');
        const favCntSpan = document.createElement('span');
        favCntSpan.classList.add('favCntSpan');
        favCntSpan.innerText = item.favCnt;
        if(item.isFav === 1) { // 좋아요 O
            heartIcon.classList.add('fas');
        }else { // 좋아요 X
            heartIcon.classList.add('far');
        }
        heartIcon.addEventListener('click', () => {
            item.isFav = 1- item.isFav;
            fetch(`/feed/fav?ifeed=${item.ifeed}&type=${item.isFav}`)
                // 클로져. heartIcon 에 이벤트 추가살 때 잠시 고정..? 해준다.
                .then(res => res.json())
                .then(myJson => {
                    if(myJson === 1) {
                        switch (item.isFav) {
                            case 0:
                                heartIcon.classList.remove('fas');
                                heartIcon.classList.add('far');
                                favCntSpan.innerText--;
                                break;
                            case 1:
                                heartIcon.classList.remove('far');
                                heartIcon.classList.add('fas');
                                favCntSpan.innerText++;
                                break;
                        }
                    }
                })
        })
        favDiv.append(heartIcon);
        itemContainer.append(favDiv);


        favDiv.append(favCntSpan);
        itemContainer.append(favDiv);


        // 글 내용 영역
        if(item.ctnt != null) {
            const ctntDiv = document.createElement('div');
            ctntDiv.innerText = item.ctnt;
            ctntDiv.classList.add('itemCtnt');
            itemContainer.append(ctntDiv);
        }

        // 댓글 영역
        const cmtDiv = document.createElement('div');
        cmtDiv.classList.add('cmtDiv');
        const cmtListDiv = document.createElement('div');
        cmtListDiv.classList.add('cmtListDiv');
        const cmtFormDiv = document.createElement('div');
        cmtFormDiv.classList.add('cmtFormDiv');
        cmtDiv.append(cmtListDiv);
        if(item.cmt !== null && item.cmt.isMore > 1) {
            const moreCmtDiv = document.createElement('div');
            const moreCmtSpan = document.createElement('span');
            moreCmtSpan.classList.add('pointer');
            moreCmtSpan.classList.add('moreCmtSpan');
            moreCmtSpan.innerHTML = ('댓글 더보기');
            moreCmtSpan.addEventListener('click', () => {
                moreCmtSpan.remove();
                this.moreCmt(item, cmtListDiv);
            })
            moreCmtDiv.append(moreCmtSpan);
            cmtDiv.append(moreCmtDiv);
        }
        cmtDiv.append(cmtFormDiv);

        if(item.cmt != null) {
            const eachCmtDiv = this.makeEachCmt(item.cmt);
            cmtListDiv.append(eachCmtDiv);
        }

        const cmtInput = document.createElement('input');
        cmtInput.classList.add('textInput');
        cmtInput.type = 'text';
        cmtInput.placeholder = '댓글을 입력하세요.';
        cmtInput.addEventListener('keyup', () => {
            cmtBtn.disabled = cmtInput.value.length === 0;
        })
        cmtInput.addEventListener('keydown', (event) => {
            if(event.key === 'Enter') {
                cmtBtn.click();
            }
        })

        const cmtBtn = document.createElement('input');
        cmtBtn.type = 'button';
        cmtBtn.value = '등록';
        cmtBtn.disabled = true;
        cmtBtn.addEventListener('click', () => {
            const cmt = cmtInput.value;
            if(cmt.length === 0) {
                alert('댓글 내용을 작성해주세요');
                return;
            }
            const param = {
                ifeed: item.ifeed,
                cmt: cmt
            }
            fetch('cmt', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json;charset=utf-8'
                },
                body: JSON.stringify(param)
            }).then(res => res.json()).catch(err => {console.log(err)})
                .then(myJson => {
                    switch(myJson) {
                        case 0:
                            alert('댓글 작성 실패 : 오류발생');
                            break;
                        case 1:
                            // 댓글 달면 내가 단 댓글만 달기
                            const globalConstElem = document.querySelector('#globalConst');
                            const param = { ...globalConstElem.dataset } // 얘를 복사해서 쓰는것(주소값X). 원본의 cmt에 안박혀
                            param.cmt = cmtInput.value;
                            param.regdt = new Date();
                            cmtListDiv.append(this.makeEachCmt(param));

                            cmtInput.value = '';


                            // 댓글 달면 댓글 다 보이기
                            /*const moreCmt = document.querySelector('.moreCmtSpan');
                            if(moreCmt !== null) {
                                moreCmt.click();
                            }else {
                                this.moreCmt(item, cmtListDiv);
                            }*/
                            break;
                    }
                })
        })

        cmtFormDiv.append(cmtInput);
        cmtFormDiv.append(cmtBtn);

        itemContainer.append(cmtDiv);


        this.containerElem.append(itemContainer);
    },
    makeFeedList: function(data) {
        if(data.length == 0) { return; }

        for(let i=0; i<data.length; i++) {
            const item = data[i];
            this.makeFeedItem(item);
        }
        if(this.swiper != null) { this.swiper = null; }
        this.swiper = new Swiper('.swiper-container', {
            direction: 'horizontal',
            loop: false,
        });
    },
    setScrollInfinity: function(target) {
        target.addEventListener('scroll', () => {
            const {
                scrollTop,
                scrollHeight,
                clientHeight
            } = document.documentElement;

            if (scrollTop + clientHeight >= scrollHeight - 5 && this.itemLength === this.limit) {
                this.itemLength = 0;
                this.getFeedList(++this.currentPage);
            }
        }, { passive: true });
    },
    getFeedList: function(page) {
        this.showLoading();

        fetch(`${this.url}?iuserForMyFeed=${this.iuser}&page=${page}&limit=${this.limit}`)
            .then(res => res.json())
            .then(myJson => {
                console.log(myJson);
                this.itemLength = myJson.length;
                this.makeFeedList(myJson);
            }).catch(err => {
            console.log(err);
        }).then(() => {
            this.hideLoading();
        });
    },
    makeEachCmt: function(cmtDat) {
        const eachCmtDiv = document.createElement('div');
        eachCmtDiv.classList.add('eachCmtDiv');
        const cmtProfile = document.createElement('div');
        cmtProfile.classList.add('itemProfileCont');
        const cmtProfileImg = document.createElement('img');
        cmtProfileImg.classList.add('cmtProfileImg', 'pointer');
        cmtProfileImg.src = `/pic/profile/${cmtDat.iuser}/${cmtDat.writerProfile}`;
        cmtProfileImg.addEventListener('click', () => {
            moveToProfile(cmtDat.iuser);
        })
        const cmtCtntDiv = document.createElement('div');
        cmtCtntDiv.classList.add('cmtCtntDiv');
        const cmtWriterDiv = document.createElement('div');
        cmtWriterDiv.classList.add('userNmCls', 'pointer');
        cmtWriterDiv.innerHTML = `${cmtDat.writer}`;
        cmtWriterDiv.addEventListener('click', () => {
            moveToProfile(cmtDat.iuser);
        })
        const cmtCtntTextDiv = document.createElement('div');
        cmtCtntTextDiv.classList.add('cmtCtntTextDiv');
        cmtCtntTextDiv.innerHTML = `${cmtDat.cmt}`;
        const cmtRegdtDiv = document.createElement('div');
        cmtRegdtDiv.classList.add('regdtDiv');
        const cmtRegdtInfo = getDateTimeInfo(cmtDat.regdt);
        cmtRegdtDiv.innerHTML = `${cmtRegdtInfo}`;
        cmtProfile.append(cmtProfileImg);
        cmtCtntDiv.append(cmtWriterDiv);
        cmtCtntDiv.append(cmtCtntTextDiv);
        cmtCtntDiv.append(cmtRegdtDiv);
        eachCmtDiv.append(cmtProfile);
        eachCmtDiv.append(cmtCtntDiv);
        return eachCmtDiv;
    },
    moreCmt: function (FeedDat, cmtList) {
        fetch(`cmt?ifeed=${FeedDat.ifeed}`)
            .then(res => res.json())
            .then(myJson => {
                cmtList.innerHTML = '';
                for(let z = 0 ; z < myJson.length; z++) {
                    let data = myJson[z];
                    const eachCmtDiv = this.makeEachCmt(data);
                    cmtList.append(eachCmtDiv);
                }
            })
    },
    hideLoading: function() { this.loadingElem.classList.add('hide');},
    showLoading: function() { this.loadingElem.classList.remove('hide'); }
}

function getDateTimeInfo(dt) {
    const nowDt = new Date(); // 기본값은 현재 시각
    const targetDt = new Date(dt);

    const nowDtSec = parseInt(nowDt.getTime()/1000);
    const targetDtSec = parseInt(targetDt.getTime()/1000);

    const diffSec = nowDtSec - targetDtSec;
    if(diffSec < 120) {
        return '방금 전';
    }else if(diffSec < 3600) {
        return parseInt(diffSec / 60) + '분 전';
    }else if(diffSec < 86400) {
        return `${parseInt(diffSec/3600)}시간 전`;
    }else if(diffSec < 604800) {
        return `${parseInt(diffSec/86400)}일 전`;
    }else {
        return targetDt.getMonth() + "월 " + targetDt.getDay() + "일";
    }
}

function moveToProfile(iuser) {
    location.href = `/user/profile?iuser=${iuser}`
}