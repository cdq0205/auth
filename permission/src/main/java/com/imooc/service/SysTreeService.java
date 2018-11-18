package com.imooc.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.imooc.dao.SysAclMapper;
import com.imooc.dao.SysAclModuleMapper;
import com.imooc.dao.SysDeptMapper;
import com.imooc.dto.AclDTO;
import com.imooc.dto.AclModuleLevelDTO;
import com.imooc.dto.DeptLevelDTO;
import com.imooc.pojo.SysAcl;
import com.imooc.pojo.SysAclModule;
import com.imooc.pojo.SysDept;
import com.imooc.utils.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Chen.d.q
 * @dt 2018/10/31 0031 20:54
 */
@Service
public class SysTreeService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;

    @Autowired
    private SysCoreService sysCoreService;

    @Autowired
    private SysAclMapper aclMapper;

    /**
     * 获取用户对应的权限树
     * @param userId
     * @return
     */
    public List<AclModuleLevelDTO> userAclTree(int userId){

        List<SysAcl> aclList = sysCoreService.getUserAclList(userId);
        List<AclDTO> aclDTOList = Lists.newArrayList();
        for (SysAcl acl : aclList){
            AclDTO aclDTO = AclDTO.adapt(acl);
            aclDTO.setChecked(true);
            aclDTO.setHasAcl(true);
            aclDTOList.add(aclDTO);
        }
        return aclDTOListToTree(aclDTOList);
    }

    /**
     * 获取当前角色对应的权限树
     * @param roleId
     * @return
     */
    public List<AclModuleLevelDTO> roleTree(int roleId){

        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);

        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());

        List<SysAcl> aclList = aclMapper.getAll();

        List<AclDTO> aclDTOList = Lists.newArrayList();
        for (SysAcl acl : aclList){
            AclDTO aclDTO = AclDTO.adapt(acl);
            if (userAclIdSet.contains(acl.getId())){
                aclDTO.setHasAcl(true);
            }
            if (roleAclIdSet.contains(acl.getId())){
                aclDTO.setChecked(true);
            }
            aclDTOList.add(aclDTO);
        }
        return aclDTOListToTree(aclDTOList);
    }

    public List<AclModuleLevelDTO> aclDTOListToTree(List<AclDTO> aclDTOList){

        if (CollectionUtils.isEmpty(aclDTOList)){
          return Lists.newArrayList();
        }

        List<AclModuleLevelDTO> aclModuleLevelDTOList = aclModuleTree();

        Multimap<Integer,AclDTO> aclDTOMultimap = ArrayListMultimap.create();
        for (AclDTO aclDTO : aclDTOList){
            if (aclDTO.getStatus() == 1){
                aclDTOMultimap.put(aclDTO.getAclModuleId(),aclDTO);
            }
        }

        bindAclsWithAclModule(aclModuleLevelDTOList,aclDTOMultimap);
        return aclModuleLevelDTOList;
    }

    public void bindAclsWithAclModule( List<AclModuleLevelDTO> aclModuleLevelDTOList,Multimap<Integer,AclDTO> aclDTOMultimap){

        if (CollectionUtils.isEmpty(aclModuleLevelDTOList)){
            return;
        }
        for (AclModuleLevelDTO aclModuleLevelDTO : aclModuleLevelDTOList){

            List<AclDTO> aclDTOList = (List<AclDTO>)aclDTOMultimap.get(aclModuleLevelDTO.getId());
            if (CollectionUtils.isNotEmpty(aclDTOList)){
                Collections.sort(aclDTOList,aclSeqComparator);
                aclModuleLevelDTO.setAclList(aclDTOList);
            }
            bindAclsWithAclModule(aclModuleLevelDTO.getAclModuleList(),aclDTOMultimap);
        }
    }

    public Comparator<AclDTO> aclSeqComparator = new Comparator<AclDTO>() {
        @Override
        public int compare(AclDTO o1, AclDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    /**
     * 权限模块树入口
     * @return
     */
    public List<AclModuleLevelDTO> aclModuleTree(){

        List<SysAclModule> aclModuleList = sysAclModuleMapper.getAllAclModule();

        List<AclModuleLevelDTO> aclModuleLevelDTOList = Lists.newArrayList();

        for (SysAclModule aclModule : aclModuleList){
            AclModuleLevelDTO aclModuleLevelDTO = AclModuleLevelDTO.adapt(aclModule);
            aclModuleLevelDTOList.add(aclModuleLevelDTO);
        }

        return aclModuleLevelDTOToTree(aclModuleLevelDTOList);
    }

    /**
     * 层级权限模块2层级树
     * @param aclModuleLevelDTOList
     * @return
     */
    public List<AclModuleLevelDTO> aclModuleLevelDTOToTree(List<AclModuleLevelDTO> aclModuleLevelDTOList){

        // 空树
        if (CollectionUtils.isEmpty(aclModuleLevelDTOList)) {
            return Lists.newArrayList();
        }

        Multimap<String,AclModuleLevelDTO> aclModuleLevelDTOMultimap = ArrayListMultimap.create();
        List<AclModuleLevelDTO> rootList = Lists.newArrayList();

        for (AclModuleLevelDTO aclModuleLevelDTO : aclModuleLevelDTOList){
            // 树状图:树的层级结构
            aclModuleLevelDTOMultimap.put(aclModuleLevelDTO.getLevel(),aclModuleLevelDTO);
            // 树状图:顶层树结构
            if (LevelUtil.ROOT.equals(aclModuleLevelDTO.getLevel())){
                rootList.add(aclModuleLevelDTO);
            }
        }

        // 顶层树结构排序
        Collections.sort(rootList, new Comparator<AclModuleLevelDTO>() {
            @Override
            public int compare(AclModuleLevelDTO o1, AclModuleLevelDTO o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });

        transformAclModuleTree(rootList, LevelUtil.ROOT,aclModuleLevelDTOMultimap);
        return rootList;
    }

    /**
     * 递归建树
     * @param rootList
     * @param level
     * @param aclModuleLevelDTOMultimap
     */
    public void transformAclModuleTree(List<AclModuleLevelDTO> rootList,String level,Multimap<String,AclModuleLevelDTO> aclModuleLevelDTOMultimap){

        for (int i = 0;i < rootList.size();i++){
            // 遍历顶层树结构,获取顶层树节点
            AclModuleLevelDTO aclModuleLevelDTO = rootList.get(i);
            // 获取子权限模块的级别 0.1 , 0.2 ,0.3
            String nextValue = LevelUtil.calculateLevel(level,aclModuleLevelDTO.getId());
            // 获取子分支 parentId=0.1 ,0.2, 0.3 ...
            List<AclModuleLevelDTO> aclModuleLevelDTOList = (List<AclModuleLevelDTO>)aclModuleLevelDTOMultimap.get(nextValue);

            if (CollectionUtils.isNotEmpty(aclModuleLevelDTOList)){
                // 子分支排序
                Collections.sort(aclModuleLevelDTOList,aclModuleSeqComparator);
                // 封装子分支到上一层树节点
                aclModuleLevelDTO.setAclModuleList(aclModuleLevelDTOList);

                transformAclModuleTree(aclModuleLevelDTOList,nextValue,aclModuleLevelDTOMultimap);
            }
        }
    }

    public Comparator<AclModuleLevelDTO> aclModuleSeqComparator = new Comparator<AclModuleLevelDTO>() {
        @Override
        public int compare(AclModuleLevelDTO o1, AclModuleLevelDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    /**
     * 部门树入口
     * @return
     */
    public List<DeptLevelDTO> deptTree(){

        List<SysDept> deptList = sysDeptMapper.getAllDept();

        List<DeptLevelDTO> deptLevelDTOList = Lists.newArrayList();

        for (SysDept dept : deptList){
           DeptLevelDTO deptLevelDTO = DeptLevelDTO.adapt(dept);
            deptLevelDTOList.add(deptLevelDTO);
        }

        return deptLevelDTOToTree(deptLevelDTOList);
    }

    /**
     * 层级部门2层级树
     * @param deptLevelDTOList
     * @return
     */
    public List<DeptLevelDTO> deptLevelDTOToTree(List<DeptLevelDTO> deptLevelDTOList){

        // 空树
        if (CollectionUtils.isEmpty(deptLevelDTOList)) {
            return Lists.newArrayList();
        }

        Multimap<String,DeptLevelDTO> deptLevelDTOMultimap = ArrayListMultimap.create();
        List<DeptLevelDTO> rootList = Lists.newArrayList();

        for (DeptLevelDTO deptLevelDTO : deptLevelDTOList){
            // 树状图:树的层级结构
            deptLevelDTOMultimap.put(deptLevelDTO.getLevel(),deptLevelDTO);
            // 树状图:顶层树结构
            if (LevelUtil.ROOT.equals(deptLevelDTO.getLevel())){
                rootList.add(deptLevelDTO);
            }
        }

        // 顶层树结构排序
        Collections.sort(rootList, new Comparator<DeptLevelDTO>() {
            @Override
            public int compare(DeptLevelDTO o1, DeptLevelDTO o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });

        transformDeptTree(rootList, LevelUtil.ROOT,deptLevelDTOMultimap);
        return rootList;
    }

    /**
     * 递归建树
     * @param rootList
     * @param level
     * @param deptLevelDTOMultimap
     */
    public void transformDeptTree(List<DeptLevelDTO> rootList,String level,Multimap<String,DeptLevelDTO> deptLevelDTOMultimap){

        for (int i = 0;i < rootList.size();i++){
            // 遍历顶层树结构,获取顶层树节点
            DeptLevelDTO deptLevelDTO = rootList.get(i);
            // 获取子部门的级别 0.1 , 0.2 ,0.3 ...
            String nextValue = LevelUtil.calculateLevel(level,deptLevelDTO.getId());
            // 获取子分支 parentId = 0.1 ,0.2, 0.3 ...
            List<DeptLevelDTO> deptLevelDTOList = (List<DeptLevelDTO>)deptLevelDTOMultimap.get(nextValue);

            if (CollectionUtils.isNotEmpty(deptLevelDTOList)){
                // 子分支排序
                Collections.sort(deptLevelDTOList,deptSeqComparator);
                // 封装子分支到上一层树节点
                deptLevelDTO.setDeptList(deptLevelDTOList);

                transformDeptTree(deptLevelDTOList,nextValue,deptLevelDTOMultimap);
            }
        }
    }

    public Comparator<DeptLevelDTO> deptSeqComparator = new Comparator<DeptLevelDTO>() {
        @Override
        public int compare(DeptLevelDTO o1, DeptLevelDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

}
