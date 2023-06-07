package beeguri.securitystudy.repository;

import beeguri.securitystudy.domain.MyFileList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyFileListRepository extends JpaRepository<MyFileList, Long> {

}
