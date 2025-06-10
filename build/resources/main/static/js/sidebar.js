// 🔽 드롭다운 메뉴 토글
document.querySelectorAll('.dropdown-toggle').forEach(toggle => {
  toggle.addEventListener('click', () => {
    const parent = toggle.parentElement;
    parent.classList.toggle('open');
    const arrow = toggle.querySelector('.arrow-icon');
    arrow.src = parent.classList.contains('open')
      ? '/images/toggleUp.png'
      : '/images/toggle.png';
  });
});

// 🚪 로그아웃 (타이머만 초기화)
document.querySelector('.logout-btn')?.addEventListener('click', () => {
  localStorage.removeItem('timerStartTime');  // ✅ 타이머만 삭제
  window.location.href = "/logout";           // 서버에서 쿠키 삭제
});
