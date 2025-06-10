const fileInput = document.getElementById("profileInput");
const preview = document.getElementById("profilePreview");
const placeholder = document.getElementById("photoPlaceholder");

fileInput.addEventListener("change", function () {
  const file = this.files[0];
  if (file) {
    const reader = new FileReader();

    reader.onload = function (e) {
      preview.src = e.target.result;
      preview.style.display = "block";
      placeholder.style.display = "none";
    };

    reader.readAsDataURL(file);
  }
});

let skillCount = 0;

function addSkill() {
  const container = document.getElementById('skillList');

  const skill = document.createElement('div');
  skill.className = 'skill-button';

  const input = document.createElement('input');
  input.type = 'text';
  input.className = 'skill-input';
  input.placeholder = '스킬 입력';

  const color = document.createElement('input');
  color.type = 'color';
  color.className = 'color-picker';
  color.addEventListener('input', (e) => {
    skill.style.backgroundColor = e.target.value;
  });

  skill.appendChild(input);
  skill.appendChild(color);
  container.appendChild(skill);

  // ✅ Hover 동작: 1초 후 show-delete 클래스 추가
  let hoverTimer;
  skill.addEventListener('mouseenter', () => {
    hoverTimer = setTimeout(() => {
      skill.classList.add('show-delete');

      // 클릭 시 삭제
      skill.onclick = () => skill.remove();
    }, 1000);
  });

  skill.addEventListener('mouseleave', () => {
    clearTimeout(hoverTimer);
    skill.classList.remove('show-delete');
    skill.onclick = null;
  });
}

function addProjectCard() {
  const container = document.getElementById('project-card-container');

  const wrapper = document.createElement('div');
  wrapper.classList.add('project-added-card'); // show는 나중에 추가
  wrapper.innerHTML = `
    <div class="project-label">
      <input type="text" class="project-title-input" placeholder="이름을 입력하세요" />
      <p class="project-delete">X</p>
    </div>
    <hr class="project-divider" />
    <div class="project-detail">
      <textarea class="project-description" placeholder="프로젝트 내용을 입력하세요"></textarea>
    </div>
  `;

  container.appendChild(wrapper);

  const deleteBtn = wrapper.querySelector('.project-delete');
  deleteBtn.addEventListener('click', () => {
    wrapper.remove();
  });

  // 다음 프레임에 트랜지션 효과 적용
  requestAnimationFrame(() => {
    wrapper.classList.add('show');
  });
}

document.getElementById('saveBtn').addEventListener('click', function () {
  // 1️⃣ 입력값 수집
  const name = document.querySelector('.name-input')?.value || '';
  const about = document.querySelector('.about-textarea')?.value || '';

  // 2️⃣ skills 수집
  const skills = Array.from(document.querySelectorAll('.skill-button')).map(skillEl => ({
    name: skillEl.querySelector('.skill-input')?.value || '',
    color: skillEl.querySelector('.color-picker')?.value || '#000000'
  }));

  // 3️⃣ projects 수집
  const projects = Array.from(document.querySelectorAll('.project-added-card')).map(projectEl => ({
    title: projectEl.querySelector('.project-title-input')?.value || '',
    description: projectEl.querySelector('.project-description')?.value || ''
  }));

  // 4️⃣ userId 수집 (폼 내부에 있다고 가정)
  const userId = document.querySelector('input[name="userId"]')?.value || '';

  // 5️⃣ FormData 객체 생성
  const formData = new FormData();
  formData.append('id', userId);
  formData.append('name', name);
  formData.append('about', about);
  formData.append('skills', JSON.stringify(skills));
  formData.append('projects', JSON.stringify(projects));

  // 6️⃣ 프로필 이미지 파일 추가 (있을 때만)
  const profileInput = document.getElementById('profileInput');
  if (profileInput?.files.length > 0) {
    formData.append('profileImage', profileInput.files[0]);
  }

  // 🔍 디버깅용 콘솔 출력
  console.log("🟢 userId:", userId);
  console.log("🟢 name:", name);
  console.log("🟢 about:", about);
  console.log("🟢 skills JSON:", JSON.stringify(skills));
  console.log("🟢 projects JSON:", JSON.stringify(projects));
  console.log("🟢 profileImage:", profileInput?.files[0] || "없음");
  for (let [key, value] of formData.entries()) {
    console.log(`🔸 ${key}:`, value);
  }

  // 7️⃣ CSRF 토큰/헤더 읽기
  const csrfToken = document.querySelector('meta[name="_csrf"]')?.content || '';
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';

  // 8️⃣ fetch로 FormData 전송 (헤더에 CSRF 토큰 추가!)
  fetch('/savePortfolio', {
    method: 'POST',
    body: formData,
    headers: {
      [csrfHeader]: csrfToken
    }
  })
      .then(response => {
        if (response.ok) {
          return response.json();
        }
        throw new Error('서버 응답 오류');
      })
      .then(data => {
        if (data.status === 'success') {
          window.location.href = data.redirectUrl;
        }
      })
      .catch(error => {
        console.error('❌ 저장 실패:', error);
        alert('저장 실패: ' + error.message);
      });
});


