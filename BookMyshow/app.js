// ===== CONFIG =====
const CONFIG = {
  BASE_URL: localStorage.getItem('bms_base_url') || 'http://localhost:8080',
};

function setBaseUrl(url) {
  CONFIG.BASE_URL = url.replace(/\/$/, '');
  localStorage.setItem('bms_base_url', CONFIG.BASE_URL);
}

// ===== API =====
const API = {
  async request(method, path, body) {
    const opts = {
      method,
      headers: { 'Content-Type': 'application/json' },
    };
    if (body) opts.body = JSON.stringify(body);
    try {
      const res = await fetch(CONFIG.BASE_URL + path, opts);
      const text = await res.text();
      let data;
      try { data = JSON.parse(text); } catch { data = text; }
      return { ok: res.ok, status: res.status, data };
    } catch (e) {
      return { ok: false, status: 0, data: { message: 'Cannot connect to server. Is it running at ' + CONFIG.BASE_URL + '?' } };
    }
  },
  get: (path) => API.request('GET', path),
  post: (path, body) => API.request('POST', path, body),
  put: (path, body) => API.request('PUT', path, body),
  delete: (path) => API.request('DELETE', path),

  // Movies
  movies: {
    getAll: () => API.get('/api/movie/list'),
    getById: (id) => API.get(`/api/movie/${id}`),
    search: (title) => API.get(`/api/movie/search?title=${encodeURIComponent(title)}`),
    byLanguage: (lang) => API.get(`/api/movie/language/${lang}`),
    create: (data) => API.post('/api/movie/create', data),
    update: (id, data) => API.put(`/api/movie/update/${id}`, data),
    delete: (id) => API.delete(`/api/movie/delete/${id}`),
  },
  // Users
  users: {
    getAll: () => API.get('/api/user/list'),
    getById: (id) => API.get(`/api/user/${id}`),
    create: (data) => API.post('/api/user/create', data),
    update: (data) => API.put('/api/user/update', data),
    delete: (id) => API.delete(`/api/user/delete/${id}`),
  },
  // Theaters
  theaters: {
    getAll: () => API.get('/api/theater/list'),
    getById: (id) => API.get(`/api/theater/${id}`),
    byCity: (city) => API.get(`/api/theater/city/${encodeURIComponent(city)}`),
    create: (data) => API.post('/api/theater/create', data),
  },
  // Shows
  shows: {
    getAll: () => API.get('/api/show/list'),
    getById: (id) => API.get(`/api/show/${id}`),
    create: (data) => API.post('/api/show/create', data),
  },
  // Bookings
  bookings: {
    create: (data) => API.post('/api/booking/create', data),
    getById: (id) => API.get(`/api/booking/${id}`),
    byUser: (userId) => API.get(`/api/booking/user/${userId}`),
    byNumber: (num) => API.get(`/api/booking/number/${num}`),
    cancel: (id) => API.put(`/api/booking/cancel/${id}`),
  },
};

// ===== TOAST =====
function toast(message, type = 'info') {
  let container = document.getElementById('toastContainer');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toastContainer';
    container.className = 'toast-container';
    document.body.appendChild(container);
  }
  const t = document.createElement('div');
  const icons = { success: '✅', error: '❌', info: 'ℹ️', warning: '⚠️' };
  t.className = `toast ${type}`;
  t.innerHTML = `<span>${icons[type] || 'ℹ️'}</span><span>${message}</span>`;
  container.appendChild(t);
  setTimeout(() => {
    t.style.animation = 'slideOut 0.3s ease forwards';
    setTimeout(() => t.remove(), 300);
  }, 3500);
}

// ===== MODAL =====
function openModal(id) {
  document.getElementById(id).classList.add('open');
  document.body.style.overflow = 'hidden';
}
function closeModal(id) {
  document.getElementById(id).classList.remove('open');
  document.body.style.overflow = '';
}
document.addEventListener('click', (e) => {
  if (e.target.classList.contains('modal-overlay')) {
    e.target.classList.remove('open');
    document.body.style.overflow = '';
  }
});

// ===== MOBILE NAV =====
function toggleMobileMenu() {
  const menu = document.getElementById('mobileMenu');
  if (menu) menu.classList.toggle('open');
}

// ===== LOADING STATE =====
function setLoading(btnEl, loading) {
  if (loading) {
    btnEl.dataset.originalText = btnEl.innerHTML;
    btnEl.innerHTML = `<span class="loader loader-sm"></span> Loading...`;
    btnEl.disabled = true;
  } else {
    btnEl.innerHTML = btnEl.dataset.originalText || btnEl.innerHTML;
    btnEl.disabled = false;
  }
}

// ===== MOVIE CARD HTML =====
function movieCardHTML(movie) {
  const poster = movie.posterUrl
    ? `<img src="${movie.posterUrl}" alt="${movie.movieName}" onerror="this.style.display='none';this.nextElementSibling.style.display='flex'">`
    : '';
  return `
    <div class="movie-card" onclick="window.location.href='movies.html?id=${movie.id}'">
      <div style="position:relative;">
        ${poster}
        <div class="movie-poster-placeholder" ${movie.posterUrl ? 'style="display:none"' : ''}>🎬</div>
        <div class="movie-badge">${movie.genre || 'Movie'}</div>
      </div>
      <div class="movie-info">
        <div class="movie-title">${movie.movieName || 'Unknown'}</div>
        <div class="movie-meta">
          <span class="tag">${movie.language || 'Hindi'}</span>
          <span class="tag">${movie.duration || ''}</span>
        </div>
        <button class="btn btn-primary w-full btn-sm" onclick="event.stopPropagation();window.location.href='booking.html?movieId=${movie.id}'">
          Book Tickets
        </button>
      </div>
    </div>`;
}

// ===== SKELETON CARDS =====
function skeletonCards(count, cols = 4) {
  return Array(count).fill(`
    <div class="movie-card">
      <div class="skeleton" style="aspect-ratio:2/3;"></div>
      <div style="padding:16px;display:flex;flex-direction:column;gap:10px;">
        <div class="skeleton" style="height:16px;border-radius:4px;"></div>
        <div class="skeleton" style="height:12px;border-radius:4px;width:60%;"></div>
        <div class="skeleton" style="height:36px;border-radius:8px;margin-top:4px;"></div>
      </div>
    </div>`).join('');
}

// ===== FORMAT DATE =====
function formatDate(str) {
  if (!str) return '';
  try {
    return new Date(str).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' });
  } catch { return str; }
}

function formatDateTime(str) {
  if (!str) return '';
  try {
    return new Date(str).toLocaleString('en-IN', { day: 'numeric', month: 'short', hour: '2-digit', minute: '2-digit' });
  } catch { return str; }
}

// ===== URL PARAMS =====
function getParam(name) {
  return new URLSearchParams(window.location.search).get(name);
}

// ===== ACTIVE NAV =====
function setActiveNav() {
  const page = window.location.pathname.split('/').pop() || 'index.html';
  document.querySelectorAll('.nav-links a').forEach(a => {
    const href = a.getAttribute('href');
    if (href === page || (page === '' && href === 'index.html')) {
      a.classList.add('active');
    }
  });
}
document.addEventListener('DOMContentLoaded', setActiveNav);
