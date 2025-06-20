import React, { useContext } from 'react';
import { NavLink, Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';
import { Button } from './Button'; // Importar o componente Button
// import './Navbar.css'; // Removido: Usar classes globais/Tailwind

/**
 * Barra de navegação superior responsiva.
 * Exibe links diferentes com base no estado de autenticação do usuário.
 */
const Navbar: React.FC = () => {
  // Obter estado de autenticação e função de logout do contexto
  const { token, user, logout, role } = useContext(AuthContext);
  const navigate = useNavigate();

  // Função para lidar com o logout
  const handleLogout = () => {
    logout();
    navigate('/login', { replace: true }); // Redireciona para login após logout
    // Opcional: Mostrar toast de sucesso no logout
  };

  // Função auxiliar para obter o path do dashboard (pode ser movida para utils se usada em mais lugares)
  const getDefaultDashboardPath = (userRole: string | null | undefined): string => {
    switch (userRole) {
      case 'CLIENTE': return '/dashboard/cliente';
      case 'PROFISSIONAL': return '/dashboard/profissional';
      case 'DONO_SALAO': return '/dashboard/owner';
      case 'ADMIN': return '/dashboard/admin';
      default: return '/'; // Fallback improvável se logado
    }
  };

  return (
    // Container da Navbar com estilos base
    <nav className="bg-background-card shadow-md sticky top-0 z-50">
      <div className="container mx-auto px-4 flex justify-between items-center h-16"> {/* Altura fixa e padding */}

        {/* Brand/Logo - Link para a página inicial */}
        <Link to="/" className="text-xl font-bold text-accent hover:text-accent-hover">
          Organizô
        </Link>

        {/* Links de Navegação (Esquerda/Centro) */}
        {/* TODO: Adicionar menu hamburguer para mobile */}
        <div className="hidden md:flex space-x-6 items-center"> {/* Esconder em mobile, espaçamento em desktop */}
          <NavLink
            to="/"
            className={({ isActive }) =>
              `text-text hover:text-accent ${isActive ? 'font-semibold text-accent' : ''}`
            }
            end // Garante que só fica ativo na raiz exata
          >
            Inicial
          </NavLink>
          <NavLink
            to="/contato" // TODO: Criar página de Contato
            className={({ isActive }) =>
              `text-text hover:text-accent ${isActive ? 'font-semibold text-accent' : ''}`
            }
          >
            Contato
          </NavLink>
          {/* Adicionar outros links principais aqui se necessário */}
        </div>

        {/* Ações do Usuário (Direita) */}
        <div className="flex items-center space-x-3">
          {token ? ( // Se o usuário ESTÁ logado
            <>
              {/* Link para o Dashboard do usuário */}
              <NavLink
                to={getDefaultDashboardPath(role)}
                className={({ isActive }) =>
                  `text-sm text-text hover:text-accent ${isActive ? 'font-semibold text-accent' : ''}`
                }
              >
                Meu Painel
                {/* Opcional: Mostrar nome do usuário: {user?.nome} */}
              </NavLink>
              {/* Botão de Logout */}
              <Button variant="secondary" size="sm" onClick={handleLogout}>
                Sair
              </Button>
            </>
          ) : ( // Se o usuário NÃO ESTÁ logado
            <>
              <Link to="/login">
                <Button variant="secondary" size="sm">
                  Login
                </Button>
              </Link>
              <Link to="/registro/cliente"> {/* Aponta para registro de cliente */}
                <Button variant="primary" size="sm">
                  Registre-se
                </Button>
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;

