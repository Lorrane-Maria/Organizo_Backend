// src/pages/LandingPage.tsx
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './LandingPage.css'; // estilos comentados e responsivos

interface NewsItem { title: string; url: string; imageUrl: string; }

/**
 * Página inicial de apresentação do Organizô,
 * com um feed de notícias e botão de login único.
 */
export default function LandingPage() {
  const [news, setNews] = useState<NewsItem[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    // Exemplo: buscar notícias mockadas
    setNews([
      {
        title: 'Top 5 Tendências de Corte para 2025',
        url: 'https://exemplo.com/tendencias-2025',
        imageUrl: 'https://via.placeholder.com/150?text=Corte+2025'
      },
      {
        title: 'Como Cuidar dos Cachos no Verão',
        url: 'https://exemplo.com/cachos-verao',
        imageUrl: 'https://via.placeholder.com/150?text=Cachos'
      },
      {
        title: 'Coloração: Dicas para Manter a Cor Vibrante',
        url: 'https://exemplo.com/coloracao-vibrante',
        imageUrl: 'https://via.placeholder.com/150?text=Coloração'
      }
    ]);
  }, []);

  return (
    <main className="landing-container">
      <header className="landing-header">
        <h1>Organizô</h1>
        <p>Seu salão, na palma da mão</p>
        <button
          className="btn btn-primary"
          onClick={() => navigate('/login')}
        >
          Login
        </button>
      </header>

      <section className="landing-news">
        <h2>Notícias & Tendências</h2>
        <div className="news-grid">
          {news.map((item, i) => (
            <a
              key={i}
              href={item.url}
              target="_blank"
              rel="noopener noreferrer"
              className="news-card"
            >
              <img src={item.imageUrl} alt={item.title} />
              <h3>{item.title}</h3>
            </a>
          ))}
        </div>
      </section>
    </main>
  );
}
