// ✅ 검색창 토글 함수
function toggleKeywordSearchBox() {
    $('#keyword_search_box').toggle();
    $('#keywordInput').val('').focus();

    if (!$('#keyword_search_box').is(':visible')) {
        $.get('/contest/filter', { filter: 'all' }, function(contests){
            renderContests(contests, true);
        });
    }
}

// ✅ renderContests: 애니메이션 여부에 따라
function renderContests(contests, animate = true){
    const container = $('#allContestsList');

    if (animate) {
        container.removeClass('fade-in').addClass('fade-out');
        setTimeout(() => {
        updateContestHtml(container, contests);
        container.removeClass('fade-out').addClass('fade-in');
        setTimeout(() => container.removeClass('fade-in'), 400);
    }, 400);
    } else {
        updateContestHtml(container, contests);
    }
}

// ✅ HTML 렌더링 함수 (검색 결과 없을 때도 공간 유지!)
function updateContestHtml(container, contests){
    let html = '';

    if (contests.length === 0) {
    html = `
        <div class="no-results" style="
          width: 100%;
          min-height: 300px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 1.1rem;
          color: #888;
        ">
          검색 결과가 없습니다.
        </div>`;
    } else {
        contests.forEach(contest => {
            const info = contest.info || {};
            html += `
              <div class="card_wrapper">
                <div class="card_inner">
                  <div class="card_front">
                    <img src="${contest.img_src}" alt="포스터">
                    <div class="card_tag">#${info['분야'] || ''}</div>
                    <div class="card_title">${contest.title}</div>
                    <div class="card_dday">D-${contest.dday}</div>
                  </div>
                  <div class="card_back">
                    <p>총 상금: ${contest.prize || '정보없음'}</p>
                    <p>지원자격: ${info['응모대상'] || '정보없음'}</p>
                    <p>주최: ${contest.organizer || '정보없음'}</p>
                    <p>조회수: ${contest.read_count}</p>
                    ${info['홈페이지'] ? `<p><a href="${info['홈페이지']}" target="_blank">홈페이지</a></p>` : ''}
                  </div>
                </div>
              </div>`;
        });
    }
    container.html(html);
}

// ✅ 문서 준비되면
$(document).ready(function(){
    $('#keyword_search_box').hide();
    // 카드 flip
    $(document).on('click', '.card_wrapper', function(){
        $(this).toggleClass('flipped');
    });

    // 버튼 클릭
    $(document).on('click', '.filter_btn', function(){
        const filterType = $(this).data('filter');
        console.log('✅ 버튼 클릭됨:', filterType);

        if (filterType === 'keyword') {
            toggleKeywordSearchBox();
        } else {
            $('#keyword_search_box').hide();
            $.get('/contest/filter', { filter: filterType }, function(contests){
                renderContests(contests, true); // fade 효과 O
            });
        }
    });

    // ✅ 검색 버튼 클릭 시 AJAX 요청
    $(document).on('click', '#searchBtn', function(){
        const keyword = $('#keywordInput').val().trim();
        const params = keyword === '' ? { filter: 'all' } : { filter: 'keyword', keyword };
        $.get('/contest/filter', params, function(contests){
            renderContests(contests, false); // fade 효과 없이 바로 갱신
        });
    });
});