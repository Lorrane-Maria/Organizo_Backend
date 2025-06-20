import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080', // ajuste se necessário
});

export default api;
