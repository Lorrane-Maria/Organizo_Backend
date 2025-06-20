import React, {
  createContext,
  useState,
  useEffect,
  ReactNode,
  useCallback, // Importar useCallback
} from 'react';
import axios from 'axios'; // Usar instância global ou a configurada
import { useNavigate } from 'react-router-dom';
import api from '../services/api'; // Usar a instância configurada

/**
 * Define a estrutura dos dados de autenticação e as funções expostas pelo contexto.
 */
interface AuthContextData {
  token: string | null;
  role: 'CLIENTE' | 'PROFISSIONAL' | 'DONO_SALAO' | 'ADMIN' | null; // Incluído ADMIN
  loading: boolean; // Indica se o estado inicial de autenticação ainda está carregando
  login: (email: string, senha: string) => Promise<void>;
  logout: () => void;
}

/**
 * Cria o Contexto de Autenticação com valores padrão.
 */
export const AuthContext = createContext<AuthContextData>({
  token: null,
  role: null,
  loading: true, // Iniciar como true para esperar a verificação inicial
  login: async () => { throw new Error('AuthProvider não encontrado'); },
  logout: () => { throw new Error('AuthProvider não encontrado'); },
});

interface Props { children: ReactNode; }

/**
 * Provedor do Contexto de Autenticação.
 * Gerencia o estado de autenticação (token, role), interage com localStorage
 * e fornece funções de login/logout.
 */
export const AuthProvider: React.FC<Props> = ({ children }) => {
  const navigate = useNavigate();

  // Inicializa os estados como null/true. O useEffect cuidará de carregar do localStorage.
  const [token, setToken] = useState<string | null>(null);
  const [role, setRole] = useState<AuthContextData['role']>(null);
  const [loading, setLoading] = useState(true); // Começa carregando

  // Efeito para carregar o estado inicial do localStorage e configurar o Axios
  useEffect(() => {
    console.log("AuthProvider: Verificando localStorage...");
    const storedToken = localStorage.getItem('token');
    const storedRole = localStorage.getItem('role') as AuthContextData['role'];

    if (storedToken && storedRole) {
      console.log("AuthProvider: Token e Role encontrados no localStorage.", { storedRole });
      // Define o token e role no estado
      setToken(storedToken);
      setRole(storedRole);
      // Configura o header padrão do Axios com o token carregado
      api.defaults.headers.common['Authorization'] = `Bearer ${storedToken}`;
    } else {
      console.log("AuthProvider: Nenhum token/role no localStorage.");
      // Garante que não há header de autorização se não houver token
      delete api.defaults.headers.common['Authorization'];
    }

    // Finaliza o carregamento inicial após verificar o localStorage
    setLoading(false);
    console.log("AuthProvider: Carregamento inicial concluído.");

  }, []); // Array de dependências vazio para rodar apenas na montagem

  // Função de Login
  const login = useCallback(async (email: string, senha: string) => {
    console.log("AuthProvider: Tentando login...");
    // setLoading(true); // Pode ser útil para feedback visual no formulário
    try {
      const resp = await api.post<{
        token: string;
        role: AuthContextData['role'];
        // Adicionar outros dados do usuário se a API retornar (nome, id, etc)
      }>(
        // Usa a variável de ambiente para a URL da API
        // O endpoint correto é /api/auth/login conforme SecurityConfig
        `/auth/login`,
        { email, senha }
        // Não precisa definir Content-Type aqui, Axios faz por padrão para JSON
      );

      const receivedToken = resp.data.token;
      const receivedRole = resp.data.role;
      console.log("AuthProvider: Login bem-sucedido.", { receivedRole });

      // Atualiza o estado
      setToken(receivedToken);
      setRole(receivedRole);

      // Configura o header padrão do Axios e salva no localStorage
      api.defaults.headers.common['Authorization'] = `Bearer ${receivedToken}`;
      localStorage.setItem('token', receivedToken);
      localStorage.setItem('role', receivedRole);

      // Redireciona para o dashboard correto baseado no perfil
      switch (receivedRole) {
        case 'CLIENTE':
          navigate('/dashboard/cliente', { replace: true });
          break;
        case 'PROFISSIONAL':
          navigate('/dashboard/profissional', { replace: true });
          break;
        case 'DONO_SALAO':
          navigate('/dashboard/owner', { replace: true });
          break;
        case 'ADMIN':
          navigate('/dashboard/admin', { replace: true });
          break;
        default:
          navigate('/', { replace: true }); // Fallback inesperado
      }
    } catch (error) {
      console.error("AuthProvider: Erro no login:", error);
      // Limpa qualquer estado/storage em caso de falha no login
      setToken(null);
      setRole(null);
      delete api.defaults.headers.common['Authorization'];
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      // Re-lança o erro para que o componente de Login possa tratá-lo (ex: exibir toast)
      throw error;
    }
    // finally {
    //   setLoading(false);
    // }
  }, [navigate]); // navigate como dependência do useCallback

  // Função de Logout
  const logout = useCallback(() => {
    console.log("AuthProvider: Fazendo logout...");
    // Limpa o estado
    setToken(null);
    setRole(null);

    // Limpa o header do Axios e o localStorage
    delete api.defaults.headers.common['Authorization'];
    localStorage.removeItem('token');
    localStorage.removeItem('role');

    // Redireciona para a página de login
    navigate('/login', { replace: true });
  }, [navigate]); // navigate como dependência do useCallback

  // Fornece os valores e funções para os componentes filhos
  return (
    <AuthContext.Provider value={{ token, role, loading, login, logout }}>
      {!loading && children} {/* Renderiza os filhos apenas quando o carregamento inicial terminar */}
    </AuthContext.Provider>
  );
};

