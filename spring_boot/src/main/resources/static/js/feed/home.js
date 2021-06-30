const feedContainerElem = document.querySelector('#feedContainer');

//피드리스트 가져오기
function getFeedList() {
    fetch('/feed/list')
        .then(res => res.json())
        .then(myJson => {
            makeFeedList(myJson);
        })
}

function makeFeedList(data) {
    if(data.length == 0) {return;}
    let beforeifeed = 0;
    let swiperWrapperDiv = null;
    for(let i = 0; i<data.length; i++) {
        const item = data[i];
        // 각 피드당 한번만
        if(beforeifeed !== item.ifeed){
            beforeifeed = item.ifeed;

            const itemContainer = document.createElement('div');
            itemContainer.classList.add('item');

            const topDiv = document.createElement('div');
            topDiv.classList.add('topDiv');
            topDiv.innerHTML = `
            <div class="itemProfileCont">
                <img src="/pic/profile/${item.iuser}/${item.mainProfile}">
            </div>
            <div>
                <div>${item.writer}</div>
                <div>${item.location == null ? '' : item.location}</div>
            </div>
            `;

            // 스와이퍼 적용
            const imgDiv = document.createElement('div');
            imgDiv.classList.add('imgDiv');
            const swiperContainerDiv = document.createElement('div');
            swiperContainerDiv.classList.add('swiper-container');

            swiperWrapperDiv = document.createElement('div');
            swiperWrapperDiv.classList.add('swiper-wrapper');

            swiperContainerDiv.append(swiperWrapperDiv);
            imgDiv.append(swiperContainerDiv);

            itemContainer.append(topDiv);
            itemContainer.append(imgDiv);
            if(item.ctnt != null) {
                const ctntDiv = document.createElement('div');
                ctntDiv.classList.add('itemCtnt');
                ctntDiv.innerText = item.ctnt;
                itemContainer.append(ctntDiv);
            }
            feedContainerElem.append(itemContainer);
        }
        if(item.img != null) { // 여러 이미지 업로드
            const swiperSlideDiv = document.createElement('div');
            swiperSlideDiv.classList.add('swiper-slide');

            const img = document.createElement('img');
            img.src = `/pic/feed/${item.ifeed}/${item.img}`;
            swiperSlideDiv.append(img);

            swiperWrapperDiv.append(swiperSlideDiv);
        }
    }

    const swiper = new Swiper('.swiper-container', {
        // Optional parameters
        direction: 'horizontal', // 방향
        loop: false,

        // If we need pagination
        pagination: { // 페이징(밑에 점 세개) 할꺼냐
            el: '.swiper-pagination', // 엘리먼트
        },

        // Navigation arrows
        navigation: { // 화살표
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },
        // And if we need scrollbar
        scrollbar: { //점 밑에 스크롤 바
            el: '.swiper-scrollbar',
        },
        effect: 'coverflow',
        coverflowEffect: {
            rotate: 30,
            slideShadows: false,
        },
    });
}

getFeedList();

