document.addEventListener("DOMContentLoaded", () => {
  const loginBtn = document.getElementById("loginBtn");
  const signUpBtn = document.getElementById("signUpBtn");
  const contestBtn = document.getElementById("contestBtn");
  const studyBtn = document.getElementById("studyBtn");
  const mainLogo = document.getElementById("mainLogo");

  if (loginBtn) {
    loginBtn.addEventListener("click", () => {
      window.location.href = "/login";
    });
  }

  if (signUpBtn) {
    signUpBtn.addEventListener("click", () => {
      window.location.href = "/signUp";
    });
  }

  if (contestBtn) {
    contestBtn.addEventListener("click", () => {
      window.location.href = "/login";
    });
  }

  if (studyBtn) {
    studyBtn.addEventListener("click", () => {
      window.location.href = "/login";
    });
  }

  if (mainLogo) {
    mainLogo.addEventListener("click", () => {
      window.location.href = "/";
    });
  }

  document.querySelectorAll('.project-delete').forEach(btn => {
    btn.addEventListener('click', (e) => {
      const wrapper = e.target.closest('.project-added-card');
      if (wrapper) {
        wrapper.remove();
      }
    });
  });
});

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


// 1. '수정 완료' 버튼 클릭 이벤트 등록
document.getElementById('saveBtn').addEventListener('click', function () {
  // 2. 입력값 수집
  const userId = document.querySelector('input[name="userId"]')?.value || document.getElementById('profileForm').userId?.value;
  const name = document.querySelector('.name-input').value;
  const about = document.querySelector('.about-textarea').value;

  // 3. skills 수집
  const skillElements = document.querySelectorAll('.skill-button');
  const skills = [];
  skillElements.forEach(skillEl => {
    const skillName = skillEl.querySelector('.skill-input').value;
    const skillColor = skillEl.querySelector('.color-picker').value;
    skills.push({ name: skillName, color: skillColor });
  });

  // 4. projects 수집
  const projectElements = document.querySelectorAll('.project-added-card');
  const projects = [];
  projectElements.forEach(projectEl => {
    const title = projectEl.querySelector('.project-title-input').value;
    const description = projectEl.querySelector('.project-description').value;
    projects.push({ title, description });
  });

  // 5. FormData 생성 (파일 업로드 포함)
  const formData = new FormData();
  formData.append('id', userId);
  formData.append('name', name);
  formData.append('about', about);
  formData.append('skills', JSON.stringify(skills));
  formData.append('projects', JSON.stringify(projects));

  // 프로필 이미지 파일이 있으면 추가
  const profileInput = document.getElementById('profileInput');
  if (profileInput && profileInput.files.length > 0) {
    formData.append('profileImage', profileInput.files[0]);
  }

  // 6. AJAX로 서버에 POST
  fetch('/savePortfolio', {
    method: 'POST',
    body: formData
  })
      .then(response => {
        if (response.redirected) {
          window.location.href = response.url; // 저장 성공 시 포트폴리오 페이지로 이동
        } else if (response.ok) {
          alert('저장 성공!');
          window.location.href = '/portfolio';
        } else {
          alert('저장 실패!');
        }
      })
      .catch(err => {
        alert('에러 발생! ' + err);
      });
});
